package ir.co.mazar.ui.activities.deceased

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.orhanobut.hawk.Hawk
import ir.co.mazar.R
import ir.co.mazar.model.deceased.CreateDeceasedRequest
import ir.co.mazar.model.deceased.DeceasedProfileDataModel
import ir.co.mazar.model.deceased.MyDeceasedDataModel
import ir.co.mazar.ui.activities.home.HomeActivity
import ir.co.mazar.ui.callback.UploadCallBack
import ir.co.mazar.ui.callback.UploadProgress
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.*
import ir.co.mazar.utils.TarhimConfig.Companion.CHOSE_IMAGE_FROM_GALLERY
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.create_deceased.*
import kotlinx.android.synthetic.main.tarhim_dialog.view.*
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.Timer
import kotlin.collections.HashMap
import kotlin.concurrent.schedule
import kotlin.properties.Delegates


class CreateDeceasedActivity : AppCompatActivity(), UploadCallBack,
    NetworkConnectionReceiver.NetworkListener {

    companion object {
        private const val TAG = "CreateDeceased"
    }

    private var imagePath: String? = ""
    private lateinit var editBirth: String
    private lateinit var editDeath: String
    private var birthZone: String? = ""
    private var deathZone: String? = ""
    private var epochT by Delegates.notNull<Long>()
    private var editProfile = false
    private var DeceasedId: Int? = -1
    private lateinit var viewModel: HomeViewModel
    private lateinit var picker: PersianDatePickerDialog
    private lateinit var details: MyDeceasedDataModel
    private lateinit var dateMap: HashMap<Int, String>
    private var deceasedInfo: DeceasedProfileDataModel? = null
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var accessType: String
    private var listBirth: List<String>? = null
    private var listDeath: List<String>? = null
    private var unixTime: String? = ""

    private var br = NetworkConnectionReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_deceased)

        var intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(br, intentFilter)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        dateMap = HashMap()
        picker = PersianDatePickerDialog(this)
        inputMethodManager =
            this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        viewModel.ldImageUpload.observe(this, Observer {
            if (it.id != null) {
                loadingImg.visibility = View.GONE
                imagePath = it.path
                Log.e(TAG, "onViewCreated: " + it.path)
            }
        })

        if (intent?.getParcelableExtra<DeceasedProfileDataModel>("EditDeceased") != null) {
            txtToolbar.text = "???????????? ??????????????"
            TvChangeImg.text = getString(R.string.msg_edit_image)
            editProfile = true

            deceasedInfo = intent?.getParcelableExtra<DeceasedProfileDataModel>("EditDeceased")!!
            val bundle = intent.extras
            DeceasedId = bundle!!.getInt("DeceasedId")
            Hawk.put("deceasedId", DeceasedId)
            Log.e("TAG", "id in create dec activity= " + deceasedInfo)

            showDeceasedDetails(deceasedInfo!!)
        } else {
            Hawk.put("locationBurial", null)
        }
        setUpView(this.window.decorView)
        setUpSpinner()

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
        BtnExitDeceasedPage.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        BtnSaveEditUser.setOnClickListener {
            if (deceasedInfo != null) {
                showLoading(true)
                if (
                    !TextUtils.isEmpty(ETNameDeceased.text.toString()) &&
                    !TextUtils.isEmpty(ETBurialLocation.text.toString()) &&
                    !TextUtils.isEmpty(EtBirthDateDeceased.text.toString()) &&
                    !TextUtils.isEmpty(ETDeathDeceased.text.toString()) &&
                    !TextUtils.isEmpty(ETdeceasedDescription.text.toString())
                ) {
                    viewModel.requestEditDeceased(
                        CreateDeceasedRequest(
                            accessType,
                            dateMap[1]!!,
                            dateMap[2]!!,
                            ETBurialLocation.text.toString(),
                            ETdeceasedDescription.text.toString(),
                            imagePath!!,
                            0.0,
                            0.0,
                            ETNameDeceased.text.toString()
                        ), Hawk.get("deceasedId")!!
                    )
                } else {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message("???????? ???????? ???????? ???? ???? ?????????? ????????")
                        .build()

                }
            } else {
                //<editor-fold desc="Create Deceaed Profile">
                if (
                    !TextUtils.isEmpty(ETNameDeceased.text.toString()) &&
                    !TextUtils.isEmpty(ETBurialLocation.text.toString()) &&
                    !TextUtils.isEmpty(EtBirthDateDeceased.text.toString()) &&
                    !TextUtils.isEmpty(ETDeathDeceased.text.toString()) &&
                    !TextUtils.isEmpty(ETdeceasedDescription.text.toString())
                ) {

                    showConfirmDialog(
                        this,
                        R.drawable.request,
                        "???? ?????? ?????????????? ?????????? ?????????? ????????????",
                        {
                            showLoading(true)

                            viewModel.requestCreateDeceased(
                                CreateDeceasedRequest(
                                    accessType,
                                    dateMap!!.get(1)!!,
                                    dateMap!!.get(2)!!,
                                    ETBurialLocation.text.toString(),
                                    ETdeceasedDescription.text.toString(),
                                    imagePath!!,
                                    0.0,
                                    0.0,
                                    ETNameDeceased.text.toString()
                                )
                            )
                            alertDialog.dismiss()

                        },
                        {
                            showLoading(false)
                            alertDialog.dismiss()
                        })


                } else {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message("???????? ???????? ???????? ???? ???? ?????????? ????????")
                        .build()
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
                DeceasedId = it.id
//                DeceasedId=  Hawk.get("deceasedId")
                TarhimToast.Builder()
                    .setActivity(this)
                    .message("???? ???????????? ?????? ????")
                    .build()

                Handler().postDelayed({

                    startActivity(
                        Intent(this, DeceasedProfileActivity::class.java)
                            .putExtra("FromPersonal", DeceasedId)!!

                    )
                    editProfile = true
                }, 1000)

            }

        })
        viewModel.ldError.observe(this, Observer {
            Toast.makeText(this, "?????????? ?????? ?????? ???? ???? ????????!", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldEditDeceased.observe(this, Observer {

            Log.i("testTag3", "edited = " + it.toString())
            if (it != null) {
                showLoading(false)
                if (it.code == 200) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                    Handler().postDelayed({
                        DeceasedId = Hawk.get("deceasedId")

                        startActivity(
                            Intent(this, DeceasedProfileActivity::class.java)
                                .putExtra("FromPersonal", DeceasedId!!)
                        )

                    }, 500)
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        })


    }


    private fun setUpBirthDayCalendar() {
        showCalendarInDarkMode(1, EtBirthDateDeceased, 1370, 3, 13)

    }

    private fun setUpDeathDayCalendar() {
        showCalendarInDarkMode(2, ETDeathDeceased, 1370, 3, 13)

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

            viewModel.requestUploadImage(uploadFile(Uri.parse(getRealPathFromURI(dataUri!!))))
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

    fun showCalendarInDarkMode(
        status: Int,
        editText: AppCompatEditText,
        year: Int,
        month: Int,
        day: Int
    ) {
        val typeface = ResourcesCompat.getFont(this, R.font.iran_sans_medium)
        val initDate = PersianCalendar()
        initDate.setPersianDate(1370, 3, 13)
        picker = PersianDatePickerDialog(this)
            .setPositiveButtonString("??????")
            .setNegativeButton("??????")
            .setTodayButton("??????????")
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


                    var p = persianCalendar.time
                    val currentDate = Date(p.time)
                    var unixTime = currentDate.time / 1000

                    Log.e("onDateSelected", unixTime.toString())
//
                    when (status) {
                        1 -> {
                            dateMap!!.put(1, unixTime.toString())
                        }
                        2 -> {
                            dateMap!!.put(2, unixTime.toString())
                        }
                    }

                    Log.e("dateMap", "onDateSelected: " + unixTime)

                    editText.imeOptions = EditorInfo.IME_ACTION_NEXT
                    showSoftKeyboard(this@CreateDeceasedActivity)
//

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

        val url: String = java.lang.String.valueOf(details.imageurl)
        if (url.startsWith("https")) {
            imagePath = details.imageurl
        } else {
            imagePath = url.replace("http", "https")
        }


        Log.e("imagePath", "showDeceasedDetails: " + imagePath)
        Glide.with(this)
            .load(imagePath)
            .circleCrop()
            .into(IvDeceased)

        Log.e("EditDeceased", "showDeceasedDetails: " + deceasedInfo!!.birthday)
        Log.e("EditDeceased", "showDeceasedDetails: " + deceasedInfo!!.deathday)
        var dateBirthDay = Date((details.birthday).toLong())
        var dateDeathDay = Date((details.deathday).toLong())
        val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
        val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)
        var birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
        var deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

        ETNameDeceased.setText(details.name)
        EtBirthDateDeceased.setText(birthDay)

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

        dateMap.put(1, details.birthday)
        dateMap.put(2, details.deathday)

        listBirth = details.birthday?.split("/")
        listDeath = details.deathday?.split("/")

        editBirth = details.birthday!!
        ETDeathDeceased.setText(deathDay)
        editDeath = details.deathday!!
        ETBurialLocation.setText(details.deathloc)
        ETdeceasedDescription.setText(details.description)

        Log.e(TAG, "showDeceasedDetails: " + deceasedInfo)

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
