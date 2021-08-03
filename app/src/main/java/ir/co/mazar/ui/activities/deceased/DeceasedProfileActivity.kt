package ir.co.mazar.ui.activities.deceased

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.GONE
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.*
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.orhanobut.hawk.Hawk
import ir.co.mazar.R
import ir.co.mazar.model.RemindeRequestModel
import ir.co.mazar.model.deceased.DeceasedProfileDataModel
import ir.co.mazar.ui.activities.home.HomeActivity
import ir.co.mazar.ui.activities.inbox.InboxMessageActivity
import ir.co.mazar.ui.activities.invite_friend.InviteActivity
import ir.co.mazar.ui.adapter.ViewPagerAdapter
import ir.co.mazar.ui.callback.ViewPagerCallBack
import ir.co.mazar.ui.fragments.deceased.CharityFragment
import ir.co.mazar.ui.fragments.deceased.ForumFragment
import ir.co.mazar.ui.fragments.deceased.GalleryFragment
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.*
import kotlinx.android.synthetic.main.deceased_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.Timer
import kotlin.concurrent.schedule

class DeceasedProfileActivity : AppCompatActivity(), ViewPagerCallBack,
    NetworkConnectionReceiver.NetworkListener {

    companion object {
        private const val TAG = "DeceasedPageFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int? = null
    private var tabsTitle = arrayListOf<String>("تالارگفتگو", "گالری تصاویر", "خیرات")
    private lateinit var briefBio: String
    private var expandable = false
    private lateinit var locationBurial: LatLng
    private var deceasedInfo: DeceasedProfileDataModel? = null
    private var bioDeceased: String? = null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var checkFollow = false
    private var adminStatus = false
    private var br = NetworkConnectionReceiver()
    private lateinit var dialog: Dialog
    private var third: Boolean = false
    private var seventh: Boolean = false
    private var forty: Boolean = false
    private var fifth: Boolean = false
    private var anniversary: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deceased_profile)
        var intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(br, intentFilter)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        showLoading(true)

        val bundle = intent.extras
        if (intent?.extras != null) {
            if (bundle!!.getInt("FromPersonal") != 0) {
                deceasedId = bundle!!.getInt("FromPersonal")
                Log.i(
                    "testTag7",
                    "deceasedId in deceased activity personal= " + deceasedId.toString()
                )
                viewModel.requestDeceasedPersonal(deceasedId!!)
                initTabAndViewPager()
            }

        }
        if (intent?.extras != null) {
            if (bundle!!.getInt("SearchPersonal") != 0) {
                deceasedId = bundle!!.getInt("SearchPersonal")
                Log.i(
                    "testTag7",
                    "deceasedId in deceased activity search= " + deceasedId.toString()
                )
                viewModel.requestDeceasedFromSearch(deceasedId!!)
                Log.e(TAG, "onViewCreated: deceasedId search " + deceasedId)
                initTabAndViewPager()
            }

        }

        viewModel.ldDeceasedProfile.observe(this, Observer {
            showLoading(false)

            it?.let {
                val url = it.imageurl

                if (!TextUtils.isEmpty(it.isowner.toString())) {
                    adminStatus = it.isowner
                }

                if (it.latitude != null) {
                    locationBurial = LatLng(it.latitude, it.longitude)
                    btnFindBurialLocation.visibility = View.VISIBLE
                }
                deceasedInfo = it

                when (it.accesstype) {
                    AccessTypeDeceased.Public.name -> {
                        if (!it.isowner) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                        }
                        coordinateLayout.visibility = View.VISIBLE
                        TvDeseacesName.text = it.name
                        TvFollowersCount.setText(
                            "${
                                SeperateNumber().splitDigit(it.followerCount).toInt()
                            } دنبال کننده "
                        )
                        var dateBirthDay = Date((it.birthday).toLong() * 1000)
                        var dateDeathDay = Date((it.deathday).toLong() * 1000)
                        val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
                        val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

                        var birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
                        var deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

                        TvDeathDateDeseaces.text = deathDay
                        TvBornDateDeseaces.text = birthDay
                        TvBurialLocation.text = "${it.deathloc}"
                        bioDeceased = it.description
                        configBioText(it.description!!)

                        if (url.startsWith("https")) {
                            Glide.with(this)
                                .load(url)
                                .circleCrop()
                                .into(ImVDeceased)
                        } else {
                            Glide.with(this)
                                .load(url.replace("http", "https"))
                                .circleCrop()
                                .into(ImVDeceased)
                        }

                        initCollapsToolbar(this, it.imageurl!!, it.name!!)

                    }
                    AccessTypeDeceased.SemiPublic.name -> {

                        Log.e(TAG, "onCreate: " + deceasedInfo.toString())
                        if (!it.isowner!!) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                            coordinateLayout.visibility = View.GONE
                            requestFollow(it)

                        } else {
                            coordinateLayout.visibility = View.VISIBLE
                            TvDeseacesName.text = it.name
                            TvFollowersCount.setText(
                                "${
                                    SeperateNumber().splitDigit(it.followerCount).toInt()
                                } دنبال کننده "
                            )
                            var dateBirthDay = Date((it.birthday).toLong() * 1000)
                            var dateDeathDay = Date((it.deathday).toLong() * 1000)
                            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
                            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

                            var birthDay =
                                "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
                            var deathDay =
                                "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

                            TvDeathDateDeseaces.text = deathDay
                            TvBornDateDeseaces.text = birthDay
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)


                            if (url.startsWith("https")) {
                                Glide.with(this)
                                    .load(url)
                                    .circleCrop()
                                    .into(ImVDeceased)
                            } else {
                                Glide.with(this)
                                    .load(url.replace("http", "https"))
                                    .circleCrop()
                                    .into(ImVDeceased)
                            }

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)

                        }
                    }
                    AccessTypeDeceased.Private.name -> {
                        if (it.isowner!!) {
                            Log.e(TAG, "onCreate:private --------------------------------- ")
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            typeSpinner.visibility = View.VISIBLE
                            TvTypeDeceasedPage.visibility = View.VISIBLE
                            typeSpinner.text = resources.getStringArray(R.array.list_access_type)[2]
                            TvDeseacesName.text = it.name
                            val dateBirthDay = Date((it.birthday).toLong() * 1000)
                            val dateDeathDay = Date((it.deathday).toLong() * 1000)
                            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
                            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

                            val birthDay =
                                "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
                            val deathDay =
                                "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"
                            TvDeathDateDeseaces.text = deathDay
                            TvBornDateDeseaces.text = birthDay
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)

                            if (url.startsWith("https")) {
                                Glide.with(this)
                                    .load(url)
                                    .circleCrop()
                                    .into(ImVDeceased)
                            } else {
                                Glide.with(this)
                                    .load(url.replace("http", "https"))
                                    .circleCrop()
                                    .into(ImVDeceased)
                            }

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)
                        } else {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                            coordinateLayout.visibility = View.GONE
                            requestFollow(it)
                            btnRequestFollow.visibility = View.GONE
                        }

                    }
                }


            }
        })

        val moreBtn = findViewById<AppCompatImageButton>(R.id.BtnMore)
        moreBtn.setOnClickListener {
            it?.let { v ->
                showMenu(v, R.menu.common_menu)
            }
        }

        viewModel.ldDeceasedFromSearch.observe(this, Observer {
            showLoading(false)

            it?.let {

                val url = it.imageurl
                if (!TextUtils.isEmpty(it.isowner!!.toString())) {
                    adminStatus = it.isowner!!
                }
                deceasedInfo = it
                if (it.latitude != null) {
                    locationBurial = LatLng(it.latitude, it.longitude)
                    btnFindBurialLocation.visibility = View.VISIBLE
                }
                when (it.accesstype) {
                    AccessTypeDeceased.Public.name -> {
                        if (!it.isowner) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                        }
                        coordinateLayout.visibility = View.VISIBLE
                        TvDeseacesName.text = it.name
                        TvFollowersCount.setText(
                            "${
                                SeperateNumber().splitDigit(it.followerCount).toInt()
                            } دنبال کننده "
                        )
                        val dateBirthDay = Date((it.birthday).toLong())
                        val dateDeathDay = Date((it.deathday).toLong())
                        val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
                        val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

                        val birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
                        val deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

                        TvDeathDateDeseaces.text = deathDay
                        TvBornDateDeseaces.text = birthDay
                        TvBurialLocation.text = "${it.deathloc}"
                        bioDeceased = it.description
                        configBioText(it.description!!)

                        if (url.startsWith("https")) {
                            Glide.with(this)
                                .load(url)
                                .circleCrop()
                                .into(ImVDeceased)
                        } else {
                            Glide.with(this)
                                .load(url.replace("http", "https"))
                                .circleCrop()
                                .into(ImVDeceased)
                        }

                        initCollapsToolbar(this, it.imageurl!!, it.name!!)

                    }
                    AccessTypeDeceased.SemiPublic.name -> {
                        if (!it.isowner) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                            coordinateLayout.visibility = View.GONE
                            requestFollow(it)

                        } else {
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            TvFollowersCount.setText(
                                "${
                                    SeperateNumber().splitDigit(it.followerCount).toInt()
                                } دنبال کننده "
                            )
                            typeSpinner.visibility = View.VISIBLE
                            TvTypeDeceasedPage.visibility = View.VISIBLE
                            typeSpinner.text = resources.getStringArray(R.array.list_access_type)[1]
                            TvDeseacesName.text = it.name
                            val dateBirthDay = Date((it.birthday).toLong())
                            val dateDeathDay = Date((it.deathday).toLong())
                            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
                            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

                            val birthDay =
                                "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
                            val deathDay =
                                "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

                            TvDeathDateDeseaces.text = deathDay
                            TvBornDateDeseaces.text = birthDay
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)

                            if (url.startsWith("https")) {
                                Glide.with(this)
                                    .load(url)
                                    .circleCrop()
                                    .into(ImVDeceased)

                            } else {
                                Glide.with(this)
                                    .load(url.replace("http", "https"))
                                    .circleCrop()
                                    .into(ImVDeceased)
                            }

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)

                        }
                    }
                    AccessTypeDeceased.Private.name -> {
                        if (it.isowner!!) {
                            Log.e(TAG, "onCreate:private --------------------------------- ")
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            typeSpinner.visibility = View.VISIBLE
                            TvTypeDeceasedPage.visibility = View.VISIBLE
                            typeSpinner.setText(resources.getStringArray(R.array.list_access_type)[2])
                            TvDeseacesName.text = it.name
                            val dateBirthDay = Date((it.birthday).toLong())
                            val dateDeathDay = Date((it.deathday).toLong())
                            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
                            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

                            val birthDay =
                                "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
                            val deathDay =
                                "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

                            TvDeathDateDeseaces.text = deathDay
                            TvBornDateDeseaces.text = birthDay
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)

                            if (url.startsWith("https")) {
                                Glide.with(this)
                                    .load(url)
                                    .circleCrop()
                                    .into(ImVDeceased)
                            } else {
                                Glide.with(this)
                                    .load(url.replace("http", "https"))
                                    .circleCrop()
                                    .into(ImVDeceased)
                            }
                            initCollapsToolbar(this, it.imageurl!!, it.name!!)
                        } else {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                            coordinateLayout.visibility = View.GONE
                            requestFollow(it)
                            btnRequestFollow.visibility = View.GONE

                        }
                    }
                }
            }
        })

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (collapsToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(
                    collapsToolbar
                )
            ) {
                cToolbar.animate().alpha(1f).duration = 600
            } else {
                cToolbar.animate().alpha(0f).duration = 600
            }
        })


        btnFindBurialLocation.setOnClickListener {

            val uri = java.lang.String.format(
                Locale.ENGLISH,
                "http://maps.google.com/maps?q=loc:%f,%f",
                locationBurial!!.latitude,
                locationBurial!!.longitude
            )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }


        /////////////notif on

        BtnNotifBell.setOnClickListener {


//            showLoading(true)
            BtnNotifBell.setBackgroundResource(R.drawable.ic_notifications_active)

            notificationDialog()
//            else {
//                Hawk.put(NOTIFICATION_STATUS, false)
//                BtnNotifBell.setBackgroundResource(R.drawable.ic_notifications_off)
//                viewModel.requestReminder(
//                    RemindeRequestModel(
//                        true,
//                        deceasedId!!,
//                        true,
//                        true,
//                        Hawk.get(TarhimConfig.USER_NUMBER),
//                        true,
//                        true,
//                        "Push"
//                    )
//                )
//            }
        }


///////////////////////////////////
        viewModel.ldReminder.observe(this, Observer {
            it.let {
                showLoading(false)
                dialog.dismiss()
                when (it.code) {
                    200 -> {
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                            .build()
                    }
                    else ->
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                            .build()
                }
            }
        })


        BtnEditDeceased.setOnClickListener {
            Log.i("testTag7", "id= " + deceasedId.toString())
            startActivity(
                Intent(this, CreateDeceasedActivity::class.java)
                    .putExtra("DeceasedId", deceasedId)
                    .putExtra("EditDeceased", deceasedInfo)
            )

        }
        BtnEditToolbar.setOnClickListener {
            Log.i("testTag7", "id2= " + deceasedId.toString())
            startActivity(
                Intent(this, CreateDeceasedActivity::class.java)
                    .putExtra("DeceasedId", deceasedId)
                    .putExtra("EditDeceased", deceasedInfo)
            )
        }
        btnAddFriends.setOnClickListener {
            startActivity(
                Intent(this, InviteActivity::class.java)
                    .putExtra("DeceasedId", deceasedId)
            )
        }
        val arrayList: Array<String> = resources.getStringArray(R.array.list_access_type)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        btnRequestFollow.setOnClickListener {
            if (deceasedInfo!!.isrequested == null || !deceasedInfo!!.isrequested!!) {
                checkFollow = true
                showLoading(true)

                viewModel.requestFollowDeceased(deceasedId!!)


            } else if (deceasedInfo!!.isrequested!!) {
                showLoading(true)
                viewModel.requestUnFollowDeceased(deceasedId!!)

                checkFollow = false

            }
        }
        viewModel.ldFollow.observe(this, Observer {
            showLoading(false)
            if (it.code == 200) {
                viewModel.requestDeceasedPersonal(deceasedId!!)
                btnRequestFollow.setBackgroundResource(R.drawable.waiting_request_follow_shape)
                btnRequestFollow.text = "در انتظار تایید"
                btnRequestFollow.setTextColor(resources.getColor(R.color.tradewind))
                TarhimToast.Builder()
                    .setActivity(this)
                    .message(it.message)
                    .build()
            } else {
                TarhimToast.Builder()
                    .setActivity(this)
                    .message(it.message)
                    .build()
            }
        })
        viewModel.ldUnFollow.observe(this, Observer {
            showLoading(false)
            if (it.code == 200) {
                viewModel.requestDeceasedPersonal(deceasedId!!)
                btnRequestFollow.setBackgroundResource(R.drawable.shape_button)
                btnRequestFollow.text = "دنبال کردن"
                btnRequestFollow.setTextColor(resources.getColor(R.color.white))
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else {
                TarhimToast.Builder()
                    .setActivity(this)
                    .message(it.message)
                    .build()

            }
        })
        BtnInboxDeceasedProfile.setOnClickListener {
            startActivity(Intent(this, InboxMessageActivity::class.java))
        }


        TvFollowersCount.setOnClickListener {
            viewModel.requestDeceasedFollowers(deceasedId!!)
        }


        viewModel.ldDeceasedFollowers.observe(this, Observer {
            it.also {
                DialogProvider().showFollowerListDialog(this, it)
            }
        })


    }

    override fun getContent(item: Int): Fragment {
        Log.e(TAG, "getContent deceasedId: " + deceasedId)
        when (item) {
            1 -> {
                return GalleryFragment().newInstance(deceasedId!!, adminStatus)
            }
            2 -> {
                return CharityFragment().newInstance(deceasedId!!)
            }
            else -> {
                return ForumFragment().newInstance(deceasedId!!, adminStatus)
            }
        }

    }

    override fun getCount(): Int {
        return tabsTitle.size
    }


    private fun initTabAndViewPager() {

        pagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, this)
        deceasedViewPager.adapter = pagerAdapter
        TabLayoutMediator(deceasedTabLayout, deceasedViewPager) { tab, position ->
            tab.text = tabsTitle[position]

        }.attach()

        for (i in 0 until deceasedTabLayout.getTabCount()) {
            val tab = (deceasedTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, 7, 0)
            tab.requestLayout()
        }
    }

    private fun initCollapsToolbar(
        ctx: Context,
        imageDeceased: String,
        nameDeceased: String
    ) {
        if (imageDeceased.startsWith("https")) {
            Glide.with(ctx)
                .load(imageDeceased)
                .circleCrop()
                .into(IvToolbar)
        } else {
            Glide.with(ctx)
                .load(imageDeceased.replace("http", "https"))
                .circleCrop()
                .into(IvToolbar)
        }

        TvNameToolbar.text = nameDeceased
    }

    private fun configBioText(bio: String) {
        TvBioDeseaces.text = bio
        if (TvBioDeseaces.lineCount > 3) {
            btShowmore.visibility = View.VISIBLE
        }

        btShowmore.setOnClickListener {
            val countLine = TvBioDeseaces.lineCount
            if (!expandable) {
                btShowmore.visibility = View.VISIBLE
                expandable = true
                val animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", countLine)
                animation.setDuration(100).start()
                btShowmore.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_up,
                    0,
                    0,
                    0
                )
            } else {
                expandable = false
                val animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", 3)
                animation.setDuration(100).start()
                btShowmore.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_down,
                    0,
                    0,
                    0
                )
            }
        }
    }

    private fun requestFollow(deceasedInfo: DeceasedProfileDataModel) {
        val url = deceasedInfo.imageurl
        showPrivateDetailsPage(deceasedInfo)
        if ((deceasedInfo.isrequested == null || !deceasedInfo.isrequested!!) && !deceasedInfo.isfollow!!) {
            BtnNotifBell.visibility = GONE
            coordinateLayout.visibility = View.GONE
            PrivateLayout.visibility = View.VISIBLE
            btnRequestFollow.setBackgroundResource(R.drawable.shape_button)
            btnRequestFollow.text = "دنبال کردن"
            btnRequestFollow.setTextColor(resources.getColor(R.color.white))
        }
        if (deceasedInfo.isrequested != null && deceasedInfo.isrequested!! && !deceasedInfo.isfollow!!) {
            btnRequestFollow.setBackgroundResource(R.drawable.waiting_request_follow_shape)
            btnRequestFollow.text = "در انتظار تایید"
            coordinateLayout.visibility = View.GONE
            btnRequestFollow.setTextColor(resources.getColor(R.color.tradewind))
            txttitleDeceaedPage.visibility = View.VISIBLE
            BtnNotifBell.visibility = GONE
            BtnInboxDeceasedProfile.visibility = View.VISIBLE
            PrivateLayout.visibility = View.VISIBLE
        }
        if (deceasedInfo.isfollow!!) {
            TvDeseacesName.text = deceasedInfo.name
            coordinateLayout.visibility = View.VISIBLE
            val dateBirthDay = Date((deceasedInfo.birthday).toLong())
            val dateDeathDay = Date((deceasedInfo.deathday).toLong())
            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

            val birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
            val deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

            TvDeathDateDeseaces.text = birthDay
            TvBornDateDeseaces.text = deathDay
            TvBurialLocation.text = "${deceasedInfo.deathloc}"
            bioDeceased = deceasedInfo.description
            configBioText(deceasedInfo.description!!)


            if (url.startsWith("https")) {
                Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(ImVDeceased)
            } else {
                Glide.with(this)
                    .load(url.replace("http", "https"))
                    .circleCrop()
                    .into(ImVDeceased)
            }

            initCollapsToolbar(this, deceasedInfo.imageurl!!, deceasedInfo.name!!)
        }
    }

    private fun showPrivateDetailsPage(itemDeceased: DeceasedProfileDataModel) {
        val url = itemDeceased.imageurl
        if (url.startsWith("https")) {
            Glide.with(this)
                .load(url)
                .circleCrop()
                .into(ImVDeceasedPrivate)
        } else {
            Glide.with(this)
                .load(url.replace("http", "https"))
                .circleCrop()
                .into(ImVDeceasedPrivate)
        }

        TvDeseacesNamePrivate.text = itemDeceased.name
        if (itemDeceased.deathday != "") {
            val dateDeathDay = Date((itemDeceased.deathday).toLong())
            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)
            val deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"
            TvDeathDateDeseacesPrivate.text = deathDay
        }
        val dateBirthDay = Date((itemDeceased.birthday).toLong())

        val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)


        val birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"


        TvBornDateDeseacesPrivate.text = birthDay

    }


    private fun showLoading(visibility: Boolean) {
        if (visibility) {
            deceasedPagePrg.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else {
            deceasedPagePrg.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(br)
    }

    override fun networkcallback(isConnected: Boolean) {
        if (isConnected) {
            deceasedPageRoot.visibility = View.VISIBLE
            noIntenterroot.visibility = View.GONE

        } else {
            deceasedPageRoot.visibility = View.GONE
            noIntenterroot.visibility = View.VISIBLE
            Timer("Network", false).schedule(4000) {
                finishAffinity()
            }
        }
    }

    private fun notificationDialog() {

        Log.i("testTag4", "dialog")
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_notification)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        val checkBoxThird: CheckBox = dialog.findViewById(R.id.checkBoxThird)
        val checkBoxSeventh: CheckBox = dialog.findViewById(R.id.checkBoxSeventh)
        val checkBoxForty: CheckBox = dialog.findViewById(R.id.checkBoxForty)
        val checkBoxAnniversary: CheckBox = dialog.findViewById(R.id.checkBoxAnniversary)
//        val checkBoxFifth: CheckBox = dialog.findViewById(R.id.checkBoxFifth)
        val notifSave: TextView = dialog.findViewById(R.id.charitySave)

        if (checkBoxThird.isChecked) {
            third = true
        }

        if (checkBoxForty.isChecked) {
            forty = true
        }

        if (checkBoxSeventh.isChecked) {
            seventh = true
        }

        if (checkBoxAnniversary.isChecked) {
            anniversary = true
        }
//        if (checkBoxFifth.isChecked) {
//            fifth = true
//        }

        notifSave.setOnClickListener {

            if (!checkBoxThird.isChecked && !checkBoxForty.isChecked && !checkBoxSeventh.isChecked
                && !checkBoxAnniversary.isChecked
            ) {
                dialog.dismiss()
            } else {
                viewModel.requestReminder(
                    RemindeRequestModel(
                        anniversary,
                        deceasedId!!,
                        fifth,
                        forty,
                        Hawk.get(TarhimConfig.USER_NUMBER),
                        seventh,
                        third,
                        "Push"
                    )
                )
            }
        }
        dialog.show()
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.report -> {
                    Toast.makeText(this, "گزارش شما با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> throw UnsupportedOperationException("there is not this item")
            }
        }

        popup.show()
    }

}