package ir.co.tarhim.ui.fragments.deceased

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.GalleryDataModel
import ir.co.tarhim.ui.adapter.AdminGalleryRecyclerAdapter
import ir.co.tarhim.ui.adapter.GalleryRecyclerAdapter
import ir.co.tarhim.ui.callback.*
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.DialogProvider
import ir.co.tarhim.utils.TarhimConfig.Companion.CHOSE_IMAGE_FROM_GALLERY
import ir.co.tarhim.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_gallery.*
import okhttp3.MultipartBody
import java.io.File

class GalleryFragment : Fragment(), GalleryListener, PostListener, UploadCallBack, RepostListener {

    companion object {
        private const val TAG = "GalleryFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int = -1
    private var adminStatus: Boolean = false
    private lateinit var pathlist: List<String>
    private lateinit var adminGalleryAdapter: AdminGalleryRecyclerAdapter
    private lateinit var GalleryAdapter: GalleryRecyclerAdapter
    fun newInstance(id: Int, adminStatus: Boolean): GalleryFragment {
        val fragment = GalleryFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putInt("Id", id)
        args.putBoolean("AdminStatus", adminStatus)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        pathlist = ArrayList()
        deceasedId = arguments?.getInt("Id")!!
        adminStatus = arguments?.getBoolean("AdminStatus")!!

        Log.e(TAG, "onViewCreated:AdminStatus " + adminStatus)
        viewModel.requestGetGallery(deceasedId)

        viewModel.ldGetGallery.observe(viewLifecycleOwner, Observer {

            it!!.let {
                initRecycler(it)
            }

        })

        viewModel.ldImageUpload.observe(viewLifecycleOwner, Observer {
            it.also {

                Log.e(TAG, "onViewCreated: " + it.path)
                Log.e(TAG, "onViewCreated: " + it.id)
                viewModel.requestPostGallery(deceasedId, it.path)
            }
        })


        viewModel.ldPostGallery.observe(viewLifecycleOwner, Observer {
            it.also {
                when (it.code) {
                    200 -> {
                        showLoading(false, it.message)
                    }
                }
            }
        })

    }


    private fun initRecycler(paths: GalleryDataModel) {
        adminGalleryAdapter = AdminGalleryRecyclerAdapter(paths, this, this)
        GalleryAdapter = GalleryRecyclerAdapter(paths, this, this)
//        if (adminStatus) {
            galleryrecycler.adapter = adminGalleryAdapter
//        } else {
//            galleryrecycler.adapter = GalleryAdapter
//        }
        galleryrecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        galleryrecycler.layoutManager =
            GridLayoutManager(requireContext(), 3)
        adminGalleryAdapter.notifyDataSetChanged()
        GalleryAdapter.notifyDataSetChanged()
    }

    override fun galleryRecyclerCallBack(rowId: String) {
        DialogProvider().showImageDialog(requireActivity(), rowId)
    }

    override fun postcallBack(deceasedId: Int) {
        openGallery()
    }

    private fun uploadPost(file: Uri): MultipartBody.Part {
        var contentFile = File(file.path)
        var uploadFile = UploadProgress(contentFile, this)
        var body = MultipartBody.Part.createFormData("file", contentFile.name, uploadFile)

        return body
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            var dataUri = data?.data

            showLoading(true, "در حال بارگذاری")
            Log.e(
                TAG,
                "onActivityResult: " + getRealPathFromURI(dataUri)
            )

            viewModel.requestUploadImage(uploadPost(Uri.parse(getRealPathFromURI(dataUri))))

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: kotlin.Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CHOSE_IMAGE_FROM_GALLERY -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent(Intent.ACTION_PICK).also { intent ->
                        intent.type = "image/*"
                        startActivityForResult(intent, CHOSE_IMAGE_FROM_GALLERY)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        CHOSE_IMAGE_FROM_GALLERY
                    )
                }
            }
        }
    }


    private fun openGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Intent(Intent.ACTION_PICK).also { intent ->
                    intent.type = "image/*"
                    startActivityForResult(intent, CHOSE_IMAGE_FROM_GALLERY)

                }
            }

            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    CHOSE_IMAGE_FROM_GALLERY
                )
            }
        }
    }


    fun getRealPathFromURI(contentUri: Uri?): String? {
        var path: String? = null
        var cursor = activity?.contentResolver?.query(contentUri!!, null, null, null, null)
        if (cursor == null) {
            path = contentUri!!.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            path = cursor.getString(index)
            cursor.close()
        }

        return path!!
    }

    override fun updateProgress(interceptare: Int) {
        Log.e(TAG, "updateProgress: " + interceptare)
    }


    private fun showLoading(status: Boolean, message: String) {
        if (status) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            PrgPostGallery.visibility = View.VISIBLE
            TarhimToast.Builder()
                .setActivity(requireActivity())
                .message(message)
                .build()

        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            PrgPostGallery.visibility = View.GONE
            TarhimToast.Builder()
                .setActivity(requireActivity())
                .message(message)
                .build()
        }
    }

    override fun repostCallback(imgId: Int) {

    }

}
