package ir.co.tarhim.ui.activities.deceased

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CreateDeceasedRequest
import ir.co.tarhim.model.deceased.DeceasedProfileDataModel
import ir.co.tarhim.model.deceased.MyDeceasedDataModel
import ir.co.tarhim.ui.activities.home.HomeActivity
import ir.co.tarhim.ui.callback.UploadCallBack
import ir.co.tarhim.ui.callback.UploadProgress
import ir.co.tarhim.ui.fragments.deceased.MapsFragment
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.AccessTypeDeceased
import ir.co.tarhim.utils.NetworkConnectionReceiver
import ir.co.tarhim.utils.TarhimCompress
import ir.co.tarhim.utils.TarhimConfig.Companion.CHOSE_IMAGE_FROM_GALLERY
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.create_deceased.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.concurrent.schedule


class CreateDeceasedActivity : AppCompatActivity(), UploadCallBack,
    MapsFragment.DetectLocationListenr,
    NetworkConnectionReceiver.NetworkListener {

    companion object {
        private const val TAG = "CreateDeceased"

    }


    private lateinit var mapsFragment: MapsFragment
    private var imagePath: String? = ""
    private lateinit var locationBurial: LatLng
    private lateinit var editBirth: String
    private lateinit var editDeath: String
    private var editProfile = false
    private var DeceasedId: Int? = -1
    private lateinit var viewModel: HomeViewModel
    private lateinit var picker: PersianDatePickerDialog
    private lateinit var details: MyDeceasedDataModel
    private lateinit var deceasedInfo: DeceasedProfileDataModel
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var accessType: String
    private var listBirth: List<String>? = null
    private var listDeath: List<String>? = null

    private var br = NetworkConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_deceased)

        var intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(br, intentFilter)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        picker = PersianDatePickerDialog(this)
        mapsFragment = MapsFragment()
        inputMethodManager =
            this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        viewModel.ldImageUpload.observe(this, Observer {
            if (it.id != null) {
                loadingImg.visibility = View.GONE
                imagePath = it.path
                Log.e(TAG, "onViewCreated: " + it.path)
            }
        })

        setUpView(this.window.decorView)
        setUpSpinner()


        locationBurial = Hawk.get("LOCATION", LatLng(0.0, 0.0))



        if (intent?.getParcelableExtra<DeceasedProfileDataModel>("EditDeceased") != null) {
            txtToolbar.text = "ویرایش پروفایل"
            TvChangeImg.text = getString(R.string.msg_edit_image)
            editProfile = true
            deceasedInfo = intent?.getParcelableExtra<DeceasedProfileDataModel>("EditDeceased")!!
            DeceasedId = intent?.getIntExtra("DeceasedId", -1)!!
            locationBurial =
                LatLng((deceasedInfo.latitude).toDouble(), (deceasedInfo.longitude).toDouble())
            showDeceasedDetails(deceasedInfo)

        }

        EtBirthDateDeceased.setOnFocusChangeListener { view, b ->
            hideSoftKeyboard(this)
            setUpBirthDayCalendar()
        }
        ETDeathDeceased.setOnFocusChangeListener { view, b ->
            hideSoftKeyboard(this)
            setUpDeathDayCalendar()
        }

        EtBirthDateDeceased.setOnClickListener {
            setUpBirthDayCalendar()

        }
        ETDeathDeceased.setOnClickListener {
            setUpDeathDayCalendar()
        }

        BtnOpenMap.setOnClickListener {
            mapFragment.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .add(R.id.mapFragment, mapsFragment.newInstance(locationBurial))
                .commit();
        }
        BtnExitDeceasedPage.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))

        }
        BtnSaveEditUser.setOnClickListener {


            if (deceasedInfo != null) {
                showLoading(true)
//
//                DialogProvider().showConfirm(
//                    this,
//                    R.drawable.request,
//                    "از ثبت اطلاعات مطمن هستید؟",
//                    {
                        viewModel.requestEditDeceased(
                        CreateDeceasedRequest(
                            accessType,
                            EtBirthDateDeceased.text.toString(),
                            ETDeathDeceased.text.toString(),
                            ETBurialLocation.text.toString(),
                            ETdeceasedDescription.text.toString(),
                            imagePath!!,
                            locationBurial.latitude,
                            locationBurial.longitude,
                            ETNameDeceased.text.toString()
                        ), DeceasedId!!)

//                       DialogProvider().dismiss()
//                    },
//                    {
//                        DialogProvider().dismiss()
//                    }
//                )

            } else {
                //<editor-fold desc="Create Deceaed Profile">
                if (
                    ETNameDeceased.text.toString().length > 0 &&
                    ETBurialLocation.text.toString().length > 0 &&
                    EtBirthDateDeceased.text.toString().length > 0 &&
                    ETDeathDeceased.text.toString().length > 0
                ) {
                    showLoading(true)
//                    DialogProvider().showConfirm(
//                        this,
//                        R.drawable.request,
//                        "از ثبت اطلاعات متوفی مطمن هستید؟",
//                        {
                            viewModel.requestCreateDeceased(
                                CreateDeceasedRequest(
                                    accessType,
                                    EtBirthDateDeceased.text.toString(),
                                    ETDeathDeceased.text.toString(),
                                    ETBurialLocation.text.toString(),
                                    ETdeceasedDescription.text.toString(),
                                    imagePath!!,
                                    locationBurial.latitude,
                                    locationBurial.longitude,
                                    ETNameDeceased.text.toString()
                                )
                            )
//                            DialogProvider().dismiss()
//
//                        },
//                        {
//                            DialogProvider().dismiss()
//                        })



                } else {
                    Toast.makeText(this, "لطفا تمام قسمت ها را تکمیل کنید", Toast.LENGTH_SHORT)
                        .show()
                }
                //</editor-fold>
            }
        }
        TvChangeImg.setOnClickListener {
            openGallery()
        }
        viewModel.ldcreateDeceased.observe(this, Observer {

            showLoading(false)
            if (it != null) {

                Toast.makeText(this, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({

                    startActivity(
                        Intent(this, DeceasedProfileActivity::class.java)
                            .putExtra("FromPersonal", it.id!!)
                    )
                    editProfile = true
                }, 1000)

            }

        })
        viewModel.ldError.observe(this, Observer {
            Toast.makeText(this, "ورودی های خود را چک کنید!", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldEditDeceased.observe(this, Observer {

            if (it != null) {

                showLoading(false)
                if (it.code == 200) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                    Handler().postDelayed({

                        startActivity(
                            Intent(this, DeceasedProfileActivity::class.java)
                                .putExtra("FromPersonal", DeceasedId!!)
                        )

                    }, 1000)
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        })

    }


    private fun setUpBirthDayCalendar() {
        showCalendarInDarkMode(EtBirthDateDeceased, 1370, 3, 13)
    }

    private fun setUpDeathDayCalendar() {
        showCalendarInDarkMode(ETDeathDeceased, 1370, 3, 13)

    }

    private fun openGallery() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Intent(Intent.ACTION_PICK).also { intent ->
                    intent.type = "image/*"
                    startActivityForResult(intent, CHOSE_IMAGE_FROM_GALLERY)

                }
            }

            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    CHOSE_IMAGE_FROM_GALLERY
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
            CHOSE_IMAGE_FROM_GALLERY -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent(Intent.ACTION_PICK).also { intent ->
                        intent.type = "image/*"
                        startActivityForResult(intent, CHOSE_IMAGE_FROM_GALLERY)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        CHOSE_IMAGE_FROM_GALLERY
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

            var imagestream = this.contentResolver.openInputStream(dataUri!!)
            var imgBitmap =
                TarhimCompress().compressImage(BitmapFactory.decodeStream(imagestream), 500)

            Glide.with(this)
                .load(imgBitmap)
                .centerInside()
                .circleCrop()
                .into(IvDeceased)

            var byte = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte)
            val path = MediaStore.Images.Media.insertImage(
                this.contentResolver,
                getRealPathFromURI(dataUri!!),
                "",
                ""
            )

            viewModel.requestUploadImage(uploadFile(Uri.parse(path)))
        }
    }


    fun getRealPathFromURI(contentUri: Uri?): String? {
        var path: String? = null
        var cursor = this?.contentResolver?.query(contentUri!!, null, null, null, null)
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
        val typeface = ResourcesCompat.getFont(this, R.font.iran_sans_medium)
        val initDate = PersianCalendar()
        initDate.setPersianDate(1370, 3, 13)
        picker = PersianDatePickerDialog(this)
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
                    showSoftKeyboard(this@CreateDeceasedActivity)
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
                this?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

            }
            else -> {
                loadingSavePofile.visibility = View.GONE
                this?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
        var contentView = this?.findViewById<View>(android.R.id.content)
        contentView!!.viewTreeObserver.addOnGlobalLayoutListener {
            var rect = Rect()
            contentView.getWindowVisibleDisplayFrame(rect)
            var screenSize = contentView.rootView.height
            var keyHeight = screenSize - rect.height()
            if (keyHeight > screenSize * 0.15) {
                view.setOnTouchListener { view, motionEvent ->
                    hideSoftKeyboard(this)
                    false
                }
            } else {

            }


        }

    }


    private fun setUpSpinner() {
        val listAccessType: Array<String> = resources.getStringArray(R.array.list_access_type)
        var spinnerAdapter = ArrayAdapter(this, R.layout.row_spinner, listAccessType)
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

    override fun locationCallback(location: LatLng) {
        Log.e("locationCallback", "locationCallback: " + location.latitude)
        Log.e("locationCallback", "locationCallback: " + location.longitude)
        locationBurial = LatLng(location.latitude, location.longitude)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(br)
    }

    override fun networkcallback(isConnected: Boolean) {
        if (isConnected) {
            createDeceasedPageRoot.visibility = View.VISIBLE
            noIntenterroot.visibility = View.GONE

        } else {
            createDeceasedPageRoot.visibility = View.GONE
            noIntenterroot.visibility = View.VISIBLE
            Timer("Network", false).schedule(4000) {
                finishAffinity()
            }
        }
    }

    fun setUpeyboard(view:View){

    }
}
