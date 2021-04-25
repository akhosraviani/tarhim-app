package ir.co.tarhim.ui.fragments.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.ui.callback.UploadCallBack
import ir.co.tarhim.ui.callback.UploadProgress
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.BaseBottomSheetDialog
import ir.co.tarhim.utils.OnBackPressed
import ir.co.tarhim.utils.TarhimCompress
import ir.co.tarhim.utils.TarhimConfig.Companion.FIRST_VISIT
import ir.co.tarhim.utils.TarhimConfig.Companion.USER_NUMBER
import kotlinx.android.synthetic.main.edit_user_profile.*
import okhttp3.MultipartBody
import java.io.File

class EditProfileFragment : BaseBottomSheetDialog(), UploadCallBack {

    companion object {
        private const val TAG = "EditProfileFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            var bottomSheet =
                (it as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            var behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = false
            var layoutParams = bottomSheet.layoutParams
            var dispayMetric = DisplayMetrics()
            (context as Activity)?.windowManager.defaultDisplay.getMetrics(dispayMetric)

            var widthHeight = dispayMetric.heightPixels
            if (layoutParams != null) {
                layoutParams.height = widthHeight
            }
            bottomSheet.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    private lateinit var viewModel: HomeViewModel
    private val GALLERY_CODE = 101
    private lateinit var imm: InputMethodManager
    private var imagePath: String? = ""
    private lateinit var bottomSheet: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_user_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        OnBackPressed().pressedCallBack(findNavController())

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        showLoading(true)
        setUpView(requireView())
        EtUserNumber.text = Hawk.get(USER_NUMBER)
        viewModel.requestUserInfo()


        viewModel.ldUserInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                showLoading(false)
                if (it.name != null) {
                    TvEditImgUser.setText(getString(R.string.msg_edit_image))
                    ETNameUser.setText(it.name)
                    if (it.email != null) {
                        ETUserEmail.setText(it.email)
                    }
                    imagePath = it.imageurl
                    Glide.with(requireContext())
                        .load(imagePath)
                        .centerInside()
                        .circleCrop()
                        .into(IvUser)
                }
            }
        })

        viewModel.ldImageUpload.observe(viewLifecycleOwner, Observer {
            loadingImgUser.visibility = View.GONE
            if (it.id != null) {
                imagePath = it.path
                Log.e(TAG, "onViewCreated: " + it.path)
            }
        })

        viewModel.ldRegisterUser.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it.code) {
                    200 -> {
                        showLoading(false)
                        Hawk.put(FIRST_VISIT, true)
                        Toast.makeText(activity, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            findNavController().navigate(R.id.action_user_edit_fragment_to_fragment_profile)
                        }, 1000)
                    }
                    400 -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })

        TvEditImgUser.setOnClickListener {
            openGallery()
        }




        BtnSaveUser.setOnClickListener {
            if (ETNameUser.text.toString().length > 0) {

                if (ETUserEmail.text.isNullOrEmpty() ||
                    Patterns.EMAIL_ADDRESS.matcher(ETUserEmail.text!!.trim().toString()).matches()
                ) {
                    showLoading(true)
//                    DialogProvider().showConfirm(
//                        requireActivity(),
//                        R.drawable.request,
//                        "از ثبت اطلاعات خود مطمن هستید؟",
//                        {
                    viewModel.requestRegisterUser(
                        RegisterUser(
                            ETUserEmail.text.toString(),
                            imagePath!!,
                            ETNameUser.text.toString()
                        )
                    )
//                            DialogProvider().dismiss()
//
//                        },
//                        {
//                            DialogProvider().dismiss()
//                        })

                } else {
                    ETUserEmail.error = "ایمیل نامعتبر"
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "نام کاربری نمیتواند خالی باشد",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        BtnExitUserPage.setOnClickListener {
            findNavController().navigate(R.id.fragment_profile)

        }
    }


    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Intent(Intent.ACTION_PICK).also { intent ->
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_CODE)

            }
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent(Intent.ACTION_PICK).also { intent ->
                        intent.type = "image/*"
                        startActivityForResult(intent, GALLERY_CODE)

                    }
                } else {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        GALLERY_CODE
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            loadingImgUser.visibility = View.VISIBLE
            var dataUri = data?.data

            Log.e(
                TAG,
                "onActivityResult: " + realFilePath(dataUri!!)
            )
            var imagestream = requireActivity().contentResolver.openInputStream(dataUri)
            var imgBitmap =
                TarhimCompress().compressImage(BitmapFactory.decodeStream(imagestream), 1000)


            Glide.with(requireContext())
                .load(realFilePath(dataUri!!))
                .centerInside()
                .circleCrop()
                .into(IvUser)

//            var byte = ByteArrayOutputStream()
//            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte)
//            val path = MediaStore.Images.Media.insertImage(
//                requireActivity().contentResolver,
//                realFilePath(dataUri!!),
//                ETNameDeceased.text.toString(),
//                null
//            )

            viewModel.requestUploadImage(uploadFile(Uri.parse(realFilePath(dataUri!!))))

        }
    }


    fun uploadFile(fileUri: Uri): MultipartBody.Part {
        var contentFile = File(fileUri.path)
        var uploadFile = UploadProgress(contentFile, this)
        var part = MultipartBody.Part.createFormData("file", contentFile.name, uploadFile)
        return part
    }

    private fun realFilePath(contentUri: Uri): String {
        var path: String? = null
        var cursor = activity?.contentResolver?.query(contentUri, null, null, null, null)
        if (cursor == null) {
            path = contentUri.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            path = cursor.getString(index)
            cursor.close()
        }

        return path!!

    }


    override fun updateProgress(interceptare: Int) {
        Log.e(TAG, "updateProgress interceptare: " + interceptare)
    }


    private fun showLoading(status: Boolean) {
        when (status) {
            true -> {
                loadingSaveUser.visibility = View.VISIBLE
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

            }
            else -> {
                loadingSaveUser.visibility = View.GONE
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }


    private fun closeKeyboard() {
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun setUpView(view: View) {
        view.setOnTouchListener { view, motionEvent ->
            closeKeyboard()
            false
        }
    }


}