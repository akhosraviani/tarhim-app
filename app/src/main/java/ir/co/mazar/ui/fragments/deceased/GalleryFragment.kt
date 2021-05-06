package ir.co.mazar.ui.fragments.deceased

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.GalleryDataModel
import ir.co.mazar.model.deceased.ReportRequest
import ir.co.mazar.ui.adapter.GalleryRecyclerViewAdapter
import ir.co.mazar.ui.callback.GalleryListener
import ir.co.mazar.ui.callback.UploadCallBack
import ir.co.mazar.ui.callback.UploadProgress
import ir.co.mazar.ui.viewModels.DeceasedViewModel
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimConfig.Companion.CHOSE_IMAGE_FROM_GALLERY
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.gallery_image_dialog.view.*
import okhttp3.MultipartBody
import java.io.File

class GalleryFragment : Fragment(), GalleryListener, UploadCallBack {

    companion object {
        private const val TAG = "GalleryFragment"
    }

    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: HomeViewModel
    private lateinit var deceasedViewModel: DeceasedViewModel
    private var deceasedId: Int = -1
    private var adminStatus: Boolean = false
    private lateinit var alertDialog:AlertDialog
    private lateinit var listsGallery: MutableList<GalleryDataModel>
    private lateinit var adminGalleryAdapter: GalleryRecyclerViewAdapter

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
        listsGallery = arrayListOf()
        deceasedId = arguments?.getInt("Id")!!
        adminStatus = arguments?.getBoolean("AdminStatus")!!
        deceasedViewModel = ViewModelProvider(this).get(DeceasedViewModel::class.java)



        viewModel.requestGetGallery(deceasedId)


        Log.e(TAG, "onViewCreated:ListsGallery  " + listsGallery.size)
        viewModel.ldGetGallery.observe(viewLifecycleOwner, Observer {

            it!!.let {
                if (adminStatus) {
                    listsGallery.add(
                        0,
                        GalleryDataModel(
                            0,
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.shape_upload_img_gallery
                            )!!
                        )
                    )
                }
                Log.e(TAG, "onViewCreated: " + it.size)

                for (i in 0 until it.size) {
                    listsGallery.add(
                        GalleryDataModel(
                            it[i].id, it[i].imagespath
                        )
                    )
                }

                initRecycler(listsGallery)
            }

            Log.i("testTag2","path list= "+listsGallery.toString())

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
                        listsGallery.clear()
                        viewModel.requestGetGallery(deceasedId)
                        showLoading(false, it.message)
                    }
                }
            }
        })

        deceasedViewModel.ldDeletePhoto.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it.code == 200) {
                    Log.i("testTag2","hiii deleted")
                    Toast.makeText(requireActivity(), "deleted", Toast.LENGTH_SHORT).show()
//                    initRecycler(listsGallery)
//                    adminGalleryAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.ldReport.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it.code == 200) {
                    Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
            })

    }






    private fun initRecycler(paths: List<GalleryDataModel>) {

        adminGalleryAdapter = GalleryRecyclerViewAdapter(requireContext(), paths, this)
        galleryrecycler.adapter = adminGalleryAdapter
        galleryrecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        galleryrecycler.layoutManager =
            GridLayoutManager(requireContext(), 3)
        adminGalleryAdapter.notifyDataSetChanged()


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
                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        CHOSE_IMAGE_FROM_GALLERY
                    )
                }
            }
        }
    }


    public fun openGallery() {
        if (
            ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
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


    override fun galleryRecyclerCallBack(position: Int, item: GalleryDataModel) {
        if (adminStatus && position == 0) {
            openGallery()
        } else {
           showImageDialog(requireActivity(), item, adminStatus,
                {
                    viewModel.grequestReport(ReportRequest(true,"Picture",item.id))
                    alertDialog.dismiss()
                },
                {Log.i("testTag2","hi im in gallaryre")
                    Log.i("testTag2","hi"+item.imagespath.toString())
                    //inja request bede
                    deceasedViewModel.deletePhotoFromGallery( deceasedId!! , item.imagespath.toString())
                    Toast.makeText(requireActivity(), "test remove", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()

                })
        }
    }

    fun showImageDialog(
        activity: Activity, item: GalleryDataModel, adminStatus: Boolean,
        report: View.OnClickListener, remove: View.OnClickListener
    ) {
        var view: ViewGroup = activity.findViewById(android.R.id.content)
        var root =
            LayoutInflater.from(activity).inflate(R.layout.gallery_image_dialog, view, false)

        alertDialog = AlertDialog.Builder(activity).setView(root).create()
        alertDialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        if (adminStatus) {
            root.btn_report_image.visibility = View.VISIBLE
//            root.btn_remove_image.visibility = View.VISIBLE
        }
        Glide.with(activity)
            .load(item.imagespath)
            .into(root.ImgGalleryDialog)

        root.btn_report_image.setOnClickListener(report)
        root.btn_remove_image.setOnClickListener(remove)

    }

}
