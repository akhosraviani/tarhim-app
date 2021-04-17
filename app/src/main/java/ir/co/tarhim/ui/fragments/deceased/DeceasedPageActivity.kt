package ir.co.tarhim.ui.fragments.deceased

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.GONE
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.DeceasedProfileDataModel
import ir.co.tarhim.ui.activities.inbox.InboxMessageActivity
import ir.co.tarhim.ui.adapter.ViewPagerAdapter
import ir.co.tarhim.ui.callback.ViewPagerCallBack
import ir.co.tarhim.ui.fragments.add_firends.AddFriendsDialogActivity
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.AccessTypeDeceased
import kotlinx.android.synthetic.main.deceased_profile.*
import java.util.*

class DeceasedPageActivity : AppCompatActivity(), ViewPagerCallBack {

    companion object {
        private const val TAG = "DeceasedPageFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int? = null
    private var tabsTitle = arrayListOf<String>("تالارگفتگو", "گالری تصاویر", "خیرات")
    private lateinit var briefBio: String
    private var expandable = false
    private lateinit var locationBurial:LatLng
    private  var deceasedInfo: DeceasedProfileDataModel?=null
    private var bioDeceased: String? = null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var checkFollow = false
    private var adminStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deceased_profile)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        showLoading(true)



        if (intent?.extras != null) {
            deceasedId = intent?.getIntExtra("FromPersonal", -1)!!
            viewModel.requestDeceasedPersonal(deceasedId!!)
            Log.e(TAG, "onViewCreated: deceasedId" + deceasedId)
            initTabAndViewPager()
        }
        if (intent.hasExtra("SearchPersonal")) {
                deceasedId = intent?.getIntExtra("SearchPersonal", -1)!!
                viewModel.requestDeceasedFromSearch(deceasedId!!)
                initTabAndViewPager()

        }
        viewModel.ldDeceasedProfile.observe(this, Observer {
            showLoading(false)


            it?.let {
                if (!TextUtils.isEmpty(it.isowner.toString())) {
                    adminStatus = it.isowner
                }

                if (it.latitude!= null) {
                    locationBurial = LatLng(it.latitude, it.longitude)
                    btnFindBurialLocation.visibility = View.VISIBLE
                }
                deceasedInfo = it
                when (it.accesstype) {
                    AccessTypeDeceased.Public.name -> {
                        if (!it.isowner!!) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                        }
                        coordinateLayout.visibility = View.VISIBLE
                        TvDeseacesName.text = it.name
                        TvDeathDateDeseaces.text = it.birthday
                        TvBornDateDeseaces.text = it.deathday
                        TvBurialLocation.text = "${it.deathloc}"
                        bioDeceased = it.description
                        configBioText(it.description!!)
                        Glide.with(this)
                            .load(it.imageurl)
                            .circleCrop()
                            .into(ImVDeceased)

                        initCollapsToolbar(this, it.imageurl!!, it.name!!)

                    }
                    AccessTypeDeceased.SemiPublic.name -> {
                        if (!it.isowner!!) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                            requestFollow(it)

                        } else {
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            typeSpinner.visibility = View.VISIBLE
                            appCompatTextView6.visibility = View.VISIBLE
                            typeSpinner.setText(resources.getStringArray(R.array.list_type)[1])
                            TvDeseacesName.text = it.name
                            TvDeathDateDeseaces.text = it.birthday
                            TvBornDateDeseaces.text = it.deathday
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)
                            Glide.with(this)
                                .load(it.imageurl)
                                .circleCrop()
                                .into(ImVDeceased)

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)

                        }
                    }
                    AccessTypeDeceased.Private.name -> {
                        if (it.isowner!!) {
                            Log.e(TAG, "onCreate: ")
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            typeSpinner.visibility = View.VISIBLE
                            appCompatTextView6.visibility = View.VISIBLE
                            typeSpinner.setText(resources.getStringArray(R.array.list_type)[2])
                            TvDeseacesName.text = it.name
                            TvDeathDateDeseaces.text = it.birthday
                            TvBornDateDeseaces.text = it.deathday
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)
                            Glide.with(this)
                                .load(it.imageurl)
                                .circleCrop()
                                .into(ImVDeceased)

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)
                        }

                    }
                }


            }
        })
        viewModel.ldDeceasedFromSearch.observe(this, Observer {
            showLoading(false)

            it?.let {
                if (!TextUtils.isEmpty(it.isowner!!.toString())) {
                    adminStatus = it.isowner!!
                }
                deceasedInfo = it
                if (it.latitude!= null) {
                    locationBurial = LatLng(it.latitude, it.longitude)
                    btnFindBurialLocation.visibility = View.VISIBLE
                }
                when (it.accesstype) {
                    AccessTypeDeceased.Public.name -> {
                        if (!it.isowner!!) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                        }
                        coordinateLayout.visibility = View.VISIBLE
                        TvDeseacesName.text = it.name
                        TvDeathDateDeseaces.text = it.birthday
                        TvBornDateDeseaces.text = it.deathday
                        TvBurialLocation.text = "${it.deathloc}"
                        bioDeceased = it.description
                        configBioText(it.description!!)
                        Glide.with(this)
                            .load(it.imageurl)
                            .circleCrop()
                            .into(ImVDeceased)

                        initCollapsToolbar(this, it.imageurl!!, it.name!!)

                    }
                    AccessTypeDeceased.SemiPublic.name -> {
                        if (!it.isowner!!) {
                            BtnEditToolbar.visibility = View.GONE
                            BtnEditDeceased.visibility = View.GONE
                            coordinateLayout.visibility = View.GONE
                            requestFollow(it)

                        } else {
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            typeSpinner.visibility = View.VISIBLE
                            appCompatTextView6.visibility = View.VISIBLE
                            typeSpinner.setText(resources.getStringArray(R.array.list_type)[1])
                            TvDeseacesName.text = it.name
                            TvDeathDateDeseaces.text = it.birthday
                            TvBornDateDeseaces.text = it.deathday
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)
                            Glide.with(this)
                                .load(it.imageurl)
                                .circleCrop()
                                .into(ImVDeceased)

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)

                        }
                    }
                    AccessTypeDeceased.Private.name -> {
                        if (it.isowner!!) {
                            Log.e(TAG, "onCreate: ")
                            coordinateLayout.visibility = View.VISIBLE
                            btnAddFriends.visibility = View.VISIBLE
                            typeSpinner.visibility = View.VISIBLE
                            appCompatTextView6.visibility = View.VISIBLE
                            typeSpinner.setText(resources.getStringArray(R.array.list_type)[2])
                            TvDeseacesName.text = it.name
                            TvDeathDateDeseaces.text = it.birthday
                            TvBornDateDeseaces.text = it.deathday
                            TvBurialLocation.text = "${it.deathloc}"
                            bioDeceased = it.description
                            configBioText(it.description!!)
                            Glide.with(this)
                                .load(it.imageurl)
                                .circleCrop()
                                .into(ImVDeceased)

                            initCollapsToolbar(this, it.imageurl!!, it.name!!)
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



        BtnEditDeceased.setOnClickListener {

            startActivity(
                Intent(this, CreateDeceasedActivity::class.java)
                    .putExtra("DeceasedId", deceasedId)
                    .putExtra("EditDeceased", deceasedInfo)
            )

        }
        BtnEditToolbar.setOnClickListener {
            startActivity(
                Intent(this, CreateDeceasedActivity::class.java)
                    .putExtra("DeceasedId", deceasedId)
                    .putExtra("EditDeceased", deceasedInfo)
            )
        }
        btnAddFriends.setOnClickListener {
            startActivity(
                Intent(this, AddFriendsDialogActivity::class.java)
                    .putExtra("DeceasedId", deceasedId)
            )
        }
        val arrayList: Array<String> = resources.getStringArray(R.array.list_type)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        btnRequestFollow.setOnClickListener {
            if (deceasedInfo!!.isrequested == null || !deceasedInfo!!.isrequested!!) {

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
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        BtInbox.setOnClickListener {
            startActivity(Intent(this, InboxMessageActivity::class.java))
        }

    }

    override fun getContent(item: Int): Fragment {
        Log.e(TAG, "getContent deceasedId: " + deceasedId)
        when (item) {
            1 -> {
                return GalleryFragment().newInstance(deceasedId!!,adminStatus)
            }
            2 -> {
                return CharityFragment()
            }
            else -> {
                return ForumFragment().newInstance(deceasedId!!)
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
        Glide.with(ctx)
            .load(imageDeceased)
            .circleCrop()
            .into(IvToolbar)

        TvNameToolbar.text = nameDeceased
    }

    private fun configBioText(bio: String) {
        TvBioDeseaces.text = bio
        if (TvBioDeseaces.lineCount > 3) {
            btShowmore.visibility = View.VISIBLE
        }

        btShowmore.setOnClickListener {
            var countLine = TvBioDeseaces.lineCount
            if (!expandable) {
                btShowmore.visibility = View.VISIBLE
                expandable = true
                var animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", countLine)
                animation.setDuration(100).start()
                btShowmore.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_arrow_up,
                    0,
                    0,
                    0
                )
            } else {
                expandable = false
                var animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", 3)
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
        showPrivateDetailsPage(deceasedInfo)
        if ((deceasedInfo.isrequested == null || !deceasedInfo.isrequested!!) && !deceasedInfo.isfollow!!) {
            txttitleDeceaedPage.visibility = GONE
            BtnNotifBell.visibility = GONE
            BtInbox.visibility = GONE
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
            txttitleDeceaedPage.visibility = GONE
            BtnNotifBell.visibility = GONE
            BtInbox.visibility = GONE
            PrivateLayout.visibility = View.VISIBLE
        }
        if (deceasedInfo.isfollow!!) {
            TvDeseacesName.text = deceasedInfo.name
            coordinateLayout.visibility = View.VISIBLE
            TvDeathDateDeseaces.text = deceasedInfo.birthday
            TvBornDateDeseaces.text = deceasedInfo.deathday
            TvBurialLocation.text = "${deceasedInfo.deathloc}"
            bioDeceased = deceasedInfo.description
            configBioText(deceasedInfo.description!!)
            Glide.with(this)
                .load(deceasedInfo.imageurl)
                .circleCrop()
                .into(ImVDeceased)

            initCollapsToolbar(this, deceasedInfo.imageurl!!, deceasedInfo.name!!)

        }

    }

    private fun showPrivateDetailsPage(itemDeceased: DeceasedProfileDataModel) {
        Glide.with(this)
            .load(itemDeceased.imageurl)
            .circleCrop()
            .into(ImVDeceasedPrivate)

        TvDeseacesNamePrivate.text = itemDeceased.name
        TvBornDateDeseacesPrivate.text = itemDeceased.birthday
        TvDeathDateDeseacesPrivate.text = itemDeceased.deathday
    }


    private fun showLoading(visibility: Boolean) {
        if (visibility) {
            deceasedPagePrg.visibility = View.VISIBLE

        } else {
            deceasedPagePrg.visibility = View.GONE

        }
    }


    private fun showContactList() {

    }


}