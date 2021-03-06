package ir.co.mazar.ui.fragments.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import ir.co.mazar.R
import ir.co.mazar.model.user.RegisterUser
import ir.co.mazar.ui.callback.UploadCallBack
import ir.co.mazar.ui.callback.UploadProgress
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.BaseBottomSheetDialog
import ir.co.mazar.utils.OnBackPressed
import ir.co.mazar.utils.TarhimCompress
import ir.co.mazar.utils.TarhimConfig.Companion.FIRST_VISIT
import ir.co.mazar.utils.TarhimConfig.Companion.USER_NUMBER
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.edit_user_profile.*
import kotlinx.android.synthetic.main.gallery_image_dialog.view.*
import kotlinx.android.synthetic.main.tarhim_dialog.view.*
import okhttp3.MultipartBody
import java.io.File

class EditProfileFragment : BaseBottomSheetDialog(), UploadCallBack {

    companion object {
        private const val TAG = "EditProfileFragment"
    }

    fun newInstance(register: Boolean): EditProfileFragment {
        val fragment = EditProfileFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putBoolean("REGISTER", register)
        return fragment
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
    private var register: Boolean = true
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
//        if (requireArguments() != null) {
//            register = requireArguments().getBoolean("REGISTER")
//        }
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
                    TvEditImgUser.text = getString(R.string.msg_edit_image)
                    ETNameUser.setText(it.name)
                    if (it.email != null) {
                        ETUserEmail.setText(it.email)
                    }

                    val url = it.imageurl.toString()
                    if (url.startsWith("https")) {
                        Glide.with(requireContext())
                            .load(url)
                            .centerInside()
                            .circleCrop()
                            .into(IvUser)
                    } else {
                        Glide.with(requireContext())
                            .load(url.replace("http", "https"))
                            .centerInside()
                            .circleCrop()
                            .into(IvUser)
                    }


//                    imagePath = it.imageurl
//                    Glide.with(requireContext())
//                        .load(imagePath)
//                        .centerInside()
//                        .circleCrop()
//                        .into(IvUser)
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
                        Toast.makeText(activity, "???? ???????????? ?????? ????", Toast.LENGTH_SHORT).show()
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
            if (ETNameUser.text.toString().isNotEmpty()) {

                if (ETUserEmail.text.isNullOrEmpty() ||
                    Patterns.EMAIL_ADDRESS.matcher(ETUserEmail.text!!.trim().toString()).matches()
                ) {
                    showLoading(true)
                    showConfirmDialog(
                        requireActivity(),
                        R.drawable.request,
                        "???? ?????? ?????????????? ?????? ?????????? ????????????",
                        {
                            viewModel.requestRegisterUser(
                                RegisterUser(
                                    ETUserEmail.text.toString(),
                                    imagePath!!,
                                    ETNameUser.text.toString()
                                )
                            )
                            alertDialog.dismiss()
                        },
                        {
                            showLoading(false)
                            alertDialog.dismiss()
                        })

                } else {
                    ETUserEmail.error = "?????????? ??????????????"
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "?????? ???????????? ???????????????? ???????? ????????",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        BtnExitUserPage.setOnClickListener {
//            if (register) {
//                TarhimToast.Builder()
//                    .setActivity(requireActivity())
//                    .message("?????????? ???????? ???????????? ?????? ???? ?????????? ????????")
//                    .build()
//            } else {
            findNavController().navigate(R.id.fragment_profile)
//            }
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


    private lateinit var alertDialog: AlertDialog
    fun showConfirmDialog(
        activity: Activity,
        image: Int,
        message: String,
        accept: View.OnClickListener,
        cancel: View.OnClickListener
    ) {
        val viewGroup: ViewGroup = activity.findViewById(android.R.id.content)
        val view =
            LayoutInflater.from(activity).inflate(R.layout.tarhim_dialog, viewGroup, false)
        alertDialog = AlertDialog.Builder(activity).setView(view).create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.TvMessageDialog.setText(message)

        view.BtnAcceptDialog.setOnClickListener(accept)
        view.BtnCloseDialog.setOnClickListener(cancel)

        view.IvImageDialog.setBackgroundResource(image)

        alertDialog.show()
    }

}