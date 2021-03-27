package ir.co.tarhim.ui.fragments.deceased

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import ir.co.tarhim.R
import ir.co.tarhim.ui.adapter.ViewPagerAdapter
import ir.co.tarhim.ui.callback.ViewPagerCallBack
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.create_deceased.view.*
import kotlinx.android.synthetic.main.deceased_profile.*
import kotlinx.android.synthetic.main.row_right_forum.*
import java.util.*


class DeceasedPageFragment : Fragment(), ViewPagerCallBack {

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int = -1
    private var tabsTitle = arrayListOf<String>("تالارگفتگو", "گالری تصاویر", "خیرات")
    private lateinit var briefBio: String
    private var expandable = false
    private var bioDeceased:String?=null
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.deceased_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (arguments != null) {
            deceasedId = arguments?.getInt("DeceasedId")!!
            viewModel.requestDeceasedProfile(deceasedId)
        }

        initTabAndViewPager()
        viewModel.ldDeceasedProfile.observe(viewLifecycleOwner, Observer {
            Log.e("TAG", "onViewCreated: " + it.name)
            TvDeseacesName.text = it.name

            TvDeathDateDeseaces.text = "تاریخ تولد :" + it.birthday
            TvBornDateDeseaces.text = "تاریخ وفات :" + it.deathday
            TvBurialLocation.text = "${it.deathloc}"
            bioDeceased = it.description
            configBioText(it.description)
            Glide.with(requireActivity())
                .load(it.imageurl)
                .circleCrop()
                .into(ImVDeceased)

            initCollapsToolbar(requireContext(), it.imageurl, it.name)
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
    }

    override fun getContent(item: Int): Fragment {
        when (item) {
            1 -> {
                return GalleryFragment().newInstance(deceasedId)
            }
            2 -> {
                return CharityFragment()
            }
            else -> {
                return ForumFragment().newInstance(deceasedId)
            }
        }
    }

    override fun getCount(): Int {
        return tabsTitle.size
    }



    private fun initTabAndViewPager() {


        pagerAdapter = ViewPagerAdapter(this, this)
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

    private fun initCollapsToolbar(ctx: Context, imageDeceased: String, nameDeceased: String) {
        Glide.with(ctx)
            .load(imageDeceased)
            .circleCrop()
            .into(IvToolbar)

        TvNameToolbar.text = nameDeceased
    }

    private fun configBioText(bio: String) {
        TvBioDeseaces.text = bio
        if(TvBioDeseaces.lineCount>3){
            btShowmore.visibility=View.VISIBLE
        }

        btShowmore.setOnClickListener {
            var countLine = TvBioDeseaces.lineCount
            if (!expandable) {
                btShowmore.visibility = View.VISIBLE
                expandable = true
                var animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", countLine)
                animation.setDuration(100).start()
                btShowmore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0)
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
}