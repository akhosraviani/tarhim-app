package ir.co.tarhim.ui.fragments.deceased

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CreateDeceasedRequest
import ir.co.tarhim.model.deceased.MyDeceasedDataModel
import ir.co.tarhim.ui.callback.UploadCallBack
import ir.co.tarhim.ui.callback.UploadProgress
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.ToastBuilder
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.create_deceased.*
import okhttp3.MultipartBody
import java.io.File


class CreateDeceased : Fragment(), UploadCallBack {

    companion object {
        private const val TAG = "CreateDeceased"
        private const val GALLERY_CODE = 101
    }
    //35.5303902,51.3763243

    private var imagePath: String? = null
    private lateinit var location: LatLng
    private var choseDate: String? = null
    private lateinit var pathImage: String
    private lateinit var cursor: Cursor
    private lateinit var viewModel: HomeViewModel
    private lateinit var picker: PersianDatePickerDialog
    private var editDetailsStatus: Boolean? = false
    private lateinit var details:MyDeceasedDataModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_deceased, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        picker = PersianDatePickerDialog(requireContext())
        viewModel.ldImageUpload.observe(viewLifecycleOwner, Observer {
            if (it.id != null) {
                loadingImg.visibility = View.GONE
                imagePath = it.path
                Log.e(TAG, "onViewCreated: " + it.path)
            }
        })

//        if (arguments?.get("EditDetails") != null) {
//            editDetailsStatus = true
//            details= arguments?.get("DeceasedDetails") as MyDeceasedDataModel
//            showDeceasedDetails(details)
//        }

        EtBirthDateDeceased.setOnClickListener {
            showDialogCalendar(EtBirthDateDeceased)
            picker.show()
        }
        ETDeathDeceased.setOnClickListener {
            showDialogCalendar(ETDeathDeceased)
            picker.show()
        }
        BtnOpenMap.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_create_deceased_to_maps_fragment)
        }

        BtnExitDeceasedPage.setOnClickListener {
            findNavController().navigateUp()
        }
        location = Hawk.get("Location", LatLng(0.0, 0.0))

        BtnSaveEdit.setOnClickListener {


                if (
                    ETNameDeceased.text.toString().length > 0 &&
                    ETBurialLocation.text.toString().length > 0 &&
                    EtBirthDateDeceased.text.toString().length > 0 &&
                    ETDeathDeceased.text.toString().length > 0
                ) {
                    showLoading(true)
                    val birthDay = EtBirthDateDeceased.text.toString()
                    val deathDay = ETDeathDeceased.text.toString()
                    viewModel.requestCreateDeceased(
                        CreateDeceasedRequest(
                            "Public",
                            birthDay,
                            deathDay,
                            ETBurialLocation.text.toString(),
                            ETdeceasedDescription.text.toString(),
                            imagePath!!,
                            location.latitude.toLong(),
                            location.longitude.toLong(),
                            ETNameDeceased.text.toString()
                        )
                    )

                } else {
                    Toast.makeText(activity, "لطفا تمام قسمت ها را تکمیل کنید", Toast.LENGTH_SHORT)
                        .show()
                }

        }


        TvChangeImg.setOnClickListener {
            openGallery()
        }

        viewModel.ldcreateDeceased.observe(viewLifecycleOwner, Observer {

            it.let {
                when (it.code) {
                    200 -> {
                        showLoading(false)
                        Toast.makeText(activity, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            findNavController().navigateUp()
                        }, 1000)
                    }
                    400 -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }


    private fun openGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.setType("image/*")
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(galleryIntent, GALLERY_CODE)
            }

            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: kotlin.Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent(Intent.ACTION_PICK)
                    galleryIntent.setType("image/*")
                    startActivityForResult(galleryIntent, GALLERY_CODE)
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
        loadingImg.visibility = View.VISIBLE
        var dataUri = data?.data

        Log.e(
            TAG,
            "onActivityResult: " + getRealPathFromURI(dataUri!!)
        )


        viewModel.requestUploadImage(uploadFile(Uri.parse(getRealPathFromURI(dataUri!!))))

        Glide.with(requireContext())
            .load(dataUri)
            .centerInside()
            .circleCrop()
            .into(IvDeceased)

    }


    fun getRealPathFromURI(contentUri: Uri?): String? {
        var cursor: Cursor =
            contentUri?.let { activity?.contentResolver?.query(it, null, null, null, null) }!!
        cursor.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = activity?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Images.Media._ID + " = ? ",
            arrayOf(document_id),
            null
        )!!
        cursor.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

    private fun uploadFile(filePath: Uri): MultipartBody.Part {
        var file = File(filePath.path)
        var uploadFile = UploadProgress(file, this)
        var multiPart = MultipartBody.Part.createFormData("file", file.name, uploadFile)

        return multiPart
    }


    @SuppressLint("ResourceAsColor")
    private fun showDialogCalendar(editText: AppCompatEditText) {

        picker
            .setPositiveButtonString("ثبت")
            .setNegativeButton("لغو")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setShowInBottomSheet(true)
            .setPickerBackgroundDrawable(R.drawable.shape_indicator_tab_selected)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setMinYear(1300)
            .setActionTextColor(R.color.tradewind)
            .setCancelable(false)
            .setListener(object : Listener {
                override fun onDateSelected(persianCalendar: PersianCalendar) {
                    choseDate = persianCalendar.getPersianYear()
                        .toString() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay()


                    editText.setText(choseDate)

                }

                override fun onDismissed() {}
            })
    }


    override fun updateProgress(interceptare: Int) {
        Log.e(TAG, "updateProgress: " + interceptare)
    }


    private fun showLoading(status: Boolean) {
        when (status) {
            true -> {
                loadingSavePofile.visibility = View.VISIBLE
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

            }
            else -> {
                loadingSavePofile.visibility = View.GONE
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    private fun showDeceasedDetails(details: MyDeceasedDataModel) {
        Glide.with(this)
            .load(details.imageurl)
            .circleCrop()
            .into(IvDeceased)
        ETNameDeceased.setText(details.name)
        EtBirthDateDeceased.setText("سال تولد :" + details.birthday)
        ETDeathDeceased.setText("سال وفات :" + details.deathday)
        ETBurialLocation.setText(details.deathloc)
        ETdeceasedDescription.setText(details.description)
    }


}
