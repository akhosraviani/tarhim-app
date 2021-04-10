package ir.co.tarhim.ui.fragments.deceased

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CreateDeceasedRequest
import ir.co.tarhim.model.deceased.DeceasedProfileDataModel
import ir.co.tarhim.model.deceased.MyDeceasedDataModel
import ir.co.tarhim.ui.callback.UploadCallBack
import ir.co.tarhim.ui.callback.UploadProgress
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.AccessTypeDeceased
import ir.co.tarhim.utils.TarhimCompress
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.create_deceased.*
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File


class CreateDeceased : Fragment(), UploadCallBack {

    companion object {
        private const val TAG = "CreateDeceased"
        private const val GALLERY_CODE = 101
    }
    //35.5303902,51.3763243

    private var imagePath: String? = ""
    private lateinit var location: LatLng
    private lateinit var pathImage: String
    private lateinit var editBirth: String
    private lateinit var editDeath: String
    private lateinit var cursor: Cursor
    private var editProfile = false
    private var DeceasedId: Int? = -1
    private lateinit var viewModel: HomeViewModel
    private lateinit var picker: PersianDatePickerDialog
    private var editDetailsStatus: Boolean? = false
    private lateinit var details: MyDeceasedDataModel
    private lateinit var deceasedInfo: DeceasedProfileDataModel
    private lateinit var inputMethodManager: InputMethodManager
    private var yearBirth: Int? = -1
    private var monthBirth: Int? = -1
    private var dayBirth: Int? = -1
    private lateinit var accessType: String
    private var yearDeath: Int? = -1
    private var monthDeath: Int? = -1
    private var dayDeath: Int? = -1

    private var listBirth: List<String>? = null
    private var listDeath: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_deceased, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        picker = PersianDatePickerDialog(requireContext())

        inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        viewModel.ldImageUpload.observe(viewLifecycleOwner, Observer {
            if (it.id != null) {
                loadingImg.visibility = View.GONE
                imagePath = it.path
                Log.e(TAG, "onViewCreated: " + it.path)
            }
        })

        setUpView(requireView())
        setUpSpinner()



        if (arguments?.get("EditDeceased") != null) {
            txtToolbar.text = "ویرایش پروفایل"
            TvChangeImg.text = getString(R.string.msg_edit_image)
            editProfile = true
            deceasedInfo = arguments?.getParcelable("EditDeceased")!!
            DeceasedId = arguments?.getInt("DeceasedId")!!
            showDeceasedDetails(deceasedInfo)

        }

        EtBirthDateDeceased.setOnFocusChangeListener { view, b ->
            hideSoftKeyboard(requireActivity())
            setUpBirthDayCalendar()
        }
        ETDeathDeceased.setOnFocusChangeListener { view, b ->
            hideSoftKeyboard(requireActivity())
            setUpDeathDayCalendar()
        }

        EtBirthDateDeceased.setOnClickListener {
            setUpBirthDayCalendar()
//            showCalendarInDarkMode()
        }
        ETDeathDeceased.setOnClickListener {
            setUpDeathDayCalendar()
//            showCalendarInDarkMode()
        }

        BtnOpenMap.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_create_deceased_to_maps_fragment)
        }

        BtnExitDeceasedPage.setOnClickListener {
            findNavController().navigate(R.id.fragment_cemetery)
        }
        location = Hawk.get("Location", LatLng(0.0, 0.0))

        BtnSaveEditUser.setOnClickListener {
            if (editProfile) {
                showLoading(true)

                viewModel.requestEditDeceased(
                    CreateDeceasedRequest(
                        accessType,
                        EtBirthDateDeceased.text.toString(),
                        ETDeathDeceased.text.toString(),
                        ETBurialLocation.text.toString(),
                        ETdeceasedDescription.text.toString(),
                        imagePath!!,
                        0,
                        0,
                        ETNameDeceased.text.toString()
                    ), DeceasedId!!
                )

                Log.e(TAG, "onViewCreated:ETDeathDeceased " + ETDeathDeceased.text.toString())
                Log.e(TAG, "onViewCreated:ETDeathDeceased " + EtBirthDateDeceased.text.toString())
            } else {
                //<editor-fold desc="Create Deceaed Profile">
                if (
                    ETNameDeceased.text.toString().length > 0 &&
                    ETBurialLocation.text.toString().length > 0 &&
                    EtBirthDateDeceased.text.toString().length > 0 &&
                    ETDeathDeceased.text.toString().length > 0
                ) {
                    showLoading(true)

                    viewModel.requestCreateDeceased(
                        CreateDeceasedRequest(
                            "Public",
                            EtBirthDateDeceased.text.toString(),
                            ETDeathDeceased.text.toString(),
                            ETBurialLocation.text.toString(),
                            ETdeceasedDescription.text.toString(),
                            imagePath!!,
                            0,
                            0,
                            ETNameDeceased.text.toString()
                        )
                    )

                } else {
                    Toast.makeText(activity, "لطفا تمام قسمت ها را تکمیل کنید", Toast.LENGTH_SHORT)
                        .show()
                }
                //</editor-fold>
            }
        }



        TvChangeImg.setOnClickListener {
            openGallery()
        }


        viewModel.ldcreateDeceased.observe(viewLifecycleOwner, Observer {

            showLoading(false)
            it.let {

                Toast.makeText(activity, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    val args = Bundle()
                    args.putInt("LatestSearch", it.id!!)
                    findNavController().navigate(
                        R.id.action_fragment_create_deceased_to_fragment_deceased_page,
                        args
                    )

                    editProfile = true
                }, 1000)

            }

        })

        viewModel.ldError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "ورودی های خود را چک کنید!", Toast.LENGTH_SHORT).show()
        })

        viewModel.ldEditDeceased.observe(viewLifecycleOwner, Observer {
            it!!.let {
                showLoading(false)
                if (it.code == 200) {
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT)
                    Handler().postDelayed({
                        val args = Bundle()
                        args.putInt("LatestSearch", DeceasedId!!)
                        findNavController().navigate(
                            R.id.action_fragment_create_deceased_to_fragment_deceased_page,
                            args
                        )
                    }, 1000)
                } else {
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        })

    }


    private fun setUpBirthDayCalendar() {
////        if (TextUtils.isEmpty(EtBirthDateDeceased.text)) {
////            showDialogCalendar(EtBirthDateDeceased, 1370, 3, 13)
////        } else {
        showCalendarInDarkMode(EtBirthDateDeceased, 1370, 3, 13)
//
////        }

    }

    private fun setUpDeathDayCalendar() {
//        hideSoftKeyboard(requireActivity())
//        if (TextUtils.isEmpty(ETDeathDeceased.text)) {
//            showDialogCalendar(ETDeathDeceased, 1370, 3, 13)
//        } else {
        showCalendarInDarkMode(ETDeathDeceased, 1370, 3, 13)

//        }

    }

    private fun openGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Intent(Intent.ACTION_PICK).also { intent ->
                    intent.type = "image/*"
                    startActivityForResult(intent, GALLERY_CODE)

                }
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
            loadingImg.visibility = View.VISIBLE
            var dataUri = data?.data

            Log.e(
                TAG,
                "onActivityResult: " + getRealPathFromURI(dataUri!!)
            )
            var imagestream = requireActivity().contentResolver.openInputStream(dataUri)
            var imgBitmap = TarhimCompress().compressImage(BitmapFactory.decodeStream(imagestream), 500)

            Glide.with(requireContext())
                .load(getRealPathFromURI(dataUri!!))
                .centerInside()
                .circleCrop()
                .into(IvDeceased)

//            var byte = ByteArrayOutputStream()
//            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte)
//            val path = MediaStore.Images.Media.insertImage(
//                requireActivity().contentResolver,
//                imgBitmap,
//                ETNameDeceased.text.toString(),
//                null
//            )


            viewModel.requestUploadImage(uploadFile(Uri.parse(getRealPathFromURI(dataUri!!))))

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

    private fun uploadFile(filePath: Uri): MultipartBody.Part {
        var file = File(filePath.path)
        var uploadFile = UploadProgress(file, this)
        var multiPart = MultipartBody.Part.createFormData("file", file.name, uploadFile)

        return multiPart
    }


    fun showCalendarInDarkMode(editText: AppCompatEditText, year: Int, month: Int, day: Int) {
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.iran_sans_medium)
        val initDate = PersianCalendar()
        initDate.setPersianDate(1370, 3, 13)
        picker = PersianDatePickerDialog(requireContext())
            .setPositiveButtonString("ثبت")
            .setNegativeButton("لغو")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setShowInBottomSheet(true)
            .setMinYear(1300)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setInitDate(initDate)
            .setActionTextColor(Color.GRAY)
            .setTypeFace(typeface)
            .setBackgroundColor(Color.BLACK)
            .setTitleColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setPickerBackgroundDrawable(R.drawable.darkmode_bg)
            .setTitleType(PersianDatePickerDialog.DAY_MONTH_YEAR)
            .setCancelable(false)
            .setListener(object : Listener {
                override fun onDateSelected(persianCalendar: PersianCalendar) {
                    var choseDate = persianCalendar?.getPersianYear()
                        .toString() + "/" + persianCalendar?.getPersianMonth() + "/" + persianCalendar?.getPersianDay()

                    editText.setText(choseDate)

                    editText.imeOptions = EditorInfo.IME_ACTION_NEXT
                    showSoftKeyboard(requireActivity())
                }

                override fun onDismissed() {}
            })
        picker.show()
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

    private fun showDeceasedDetails(details: DeceasedProfileDataModel) {

        Glide.with(this)
            .load(details.imageurl)
            .circleCrop()
            .into(IvDeceased)

        imagePath = details.imageurl
        ETNameDeceased.setText(details.name)
        EtBirthDateDeceased.setText(details.birthday)

        when (details.accesstype) {
            AccessTypeDeceased.Public.name -> {
                AccessTypeSpinner.setSelection(0)
            }
            AccessTypeDeceased.SemiPublic.name -> {
                AccessTypeSpinner.setSelection(1)
            }
            AccessTypeDeceased.Private.name -> {
                AccessTypeSpinner.setSelection(2)
            }
        }

        listBirth = details.birthday?.split("/")
        listDeath = details.deathday?.split("/")

        editBirth = details.birthday!!
        ETDeathDeceased.setText(details.deathday)
        editDeath = details.deathday!!
        ETBurialLocation.setText(details.deathloc)
        ETdeceasedDescription.setText(details.description)
    }


    fun hideSoftKeyboard(activity: Activity) {
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    fun showSoftKeyboard(activity: Activity) {
        inputMethodManager.showSoftInput(activity.currentFocus, 0)
    }


    fun setUpView(view: View) {
        var contentView = activity?.findViewById<View>(android.R.id.content)
        contentView!!.viewTreeObserver.addOnGlobalLayoutListener {
            var rect = Rect()
            contentView.getWindowVisibleDisplayFrame(rect)
            var screenSize = contentView.rootView.height
            var keyHeight = screenSize - rect.height()
            if (keyHeight > screenSize * 0.15){
                view.setOnTouchListener { view, motionEvent ->
                    hideSoftKeyboard(requireActivity())
                    false
                }
            }else{

            }


        }

    }


    private fun setUpSpinner() {
        val listAccessType: Array<String> = resources.getStringArray(R.array.list_type)
        var spinnerAdapter = ArrayAdapter(requireContext(), R.layout.row_spinner, listAccessType)
        spinnerAdapter.setDropDownViewResource(R.layout.row_spinner)
        AccessTypeSpinner.adapter = spinnerAdapter


        AccessTypeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                when (position) {
                    0 -> {
                        accessType = AccessTypeDeceased.Public.name
                    }
                    1 -> {
                        accessType = AccessTypeDeceased.SemiPublic.name
                    }
                    2 -> {
                        accessType = AccessTypeDeceased.Private.name
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                accessType = AccessTypeDeceased.Public.name
            }
        }
    }


}
