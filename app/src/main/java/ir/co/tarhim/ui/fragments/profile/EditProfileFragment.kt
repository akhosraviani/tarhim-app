package ir.co.tarhim.ui.fragments.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.ui.callback.UploadCallBack
import ir.co.tarhim.ui.callback.UploadProgress
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.OnBackPressed
import kotlinx.android.synthetic.main.edit_user_profile.*
import okhttp3.MultipartBody
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditProfileFragment : Fragment(), UploadCallBack {

    companion object {
        private const val TAG = "EditProfileFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private val GALLERY_CODE = 101
    private lateinit var imm: InputMethodManager
    private lateinit var imagePath: String
    private lateinit var bottomSheet: FrameLayout

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
//        dialog.setOnShowListener {
//            bottomSheet =
//                ((it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?)!!
//            val behavior = BottomSheetBehavior.from(bottomSheet!!)
//            behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            behavior.isDraggable = false
//
//        }
//
//        return dialog
//
//    }


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
        EtUserNumber.text = Hawk.get("UserNumber")
        viewModel.requestUserInfo()


        viewModel.ldUserInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                showLoading(false)
                if (it.name != null ) {
                    TvEditImgUser.setText(getString(R.string.msg_edit_image))
                    ETNameUser.setText(it.name)
                    if(it.email != null) {
                        ETUserEmail.setText(it.email)
                    }
                    imagePath = it.imageurl
                    Glide.with(requireContext())
                        .load(it.imageurl)
                        .centerInside()
                        .circleCrop()
                        .into(IvUser)
                }
            }
        })

        viewModel.ldImageUpload.observe(viewLifecycleOwner, Observer {
            if (it.id != null) {
                loadingImgUser.visibility = View.GONE
                imagePath = it.path
                Log.e(TAG, "onViewCreated: " + it.path)
            }
        })

        viewModel.ldRegisterUser.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it.code) {
                    200 -> {
                        showLoading(false)
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

//                if(ETUserEmail.text.toString().length > 0){
//                    val m: Matcher = p.matcher(ETUserEmail.text.toString())
//                    if (m.find()) {
//                        showLoading(true)
//                        viewModel.requestRegisterUser(
//                            RegisterUser(
//                                ETUserEmail.text.toString(),
//                                imagePath,
//                                ETNameUser.text.toString()
//                            )
//                        )
//                    }else{
//                        Toast.makeText(requireContext(), "ایمیل نامعتبر است", Toast.LENGTH_SHORT)
//                    }
//                }else {

                showLoading(true)
                viewModel.requestRegisterUser(
                    RegisterUser(
                        ETUserEmail.text.toString(),
                        imagePath,
                        ETNameUser.text.toString()
                    )
                )

            } else {
                Toast.makeText(
                    requireContext(),
                    "نام کاربری نمیتواند خالی باشد",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        BtnExitUserPage.setOnClickListener {
            findNavController().navigateUp()

        }
    }

    private fun validateEmail(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val p: Pattern = Pattern.compile(emailPattern)

        return false
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
            var dataUri = data?.data
            loadingImgUser.visibility = View.VISIBLE
            Log.e(
                TAG, "onActivityResult: " + realFilePath(dataUri!!)
            )
            viewModel.requestUploadImage(uploadFile(Uri.parse(realFilePath(dataUri!!))))
            Glide.with(requireContext())
                .load(dataUri)
                .centerInside()
                .circleCrop()
                .into(IvUser)
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