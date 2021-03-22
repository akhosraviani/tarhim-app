package ir.co.tarhim.ui.fragments.deceased

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import ir.co.tarhim.R
import ir.co.tarhim.ui.adapter.ViewPagerAdapter
import ir.co.tarhim.ui.callback.ViewPagerCallBack
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.PersianDate
import kotlinx.android.synthetic.main.deceased_profile.*
import java.util.*


class DeceasedPageFragment : Fragment(), ViewPagerCallBack {

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int = -1
    private var tabsTitle = arrayListOf<String>("تالارگفتگو", "گالری تصاویر", "خیرات")
    private lateinit var briefBio: String
    private var expandable = false
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
        pagerAdapter = ViewPagerAdapter(this, this)
        deceasedViewPager.adapter = pagerAdapter
        TabLayoutMediator(deceasedTabLayout, deceasedViewPager) { tab, position ->
            tab.text = tabsTitle[position]
        }.attach()

        viewModel.ldDeceasedProfile.observe(viewLifecycleOwner, Observer {
            TvDeseacesName.text = it.name

            var birthDayDate = Date(it.birthday)
            var deathDayDate = Date(it.deathday)
            var ScbirthDay = PersianDate.SolarCalendar(birthDayDate)
            var ScDeathDay = PersianDate.SolarCalendar(deathDayDate)
            var birthDay = "${ScbirthDay.year}/${ScbirthDay.month}/${ScbirthDay.date}"
            var deathDay = "${ScDeathDay.year}/${ScDeathDay.month}/${ScDeathDay.date}"

            TvDeathDateDeseaces.text = "تاریخ تولد : ${birthDay}"
            TvBornDateDeseaces.text = "تاریخ وفات : ${deathDay}"

            TvBioDeseaces.text = it.description
            if (TvBioDeseaces.lineCount > 3) {
                btShowmore.visibility = View.VISIBLE
            }
            var countline = TvBioDeseaces.lineCount

            btShowmore.setOnClickListener(View.OnClickListener {
                if (!expandable) {
                    expandable = true
                    val animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", countline)
                    animation.setDuration(100).start()
                    btShowmore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_arrow_up,
                        0,
                        0,
                        0
                    )
                } else {
                    expandable = false
                    val animation = ObjectAnimator.ofInt(TvBioDeseaces, "maxLines", 3)
                    animation.setDuration(100).start()
                    btShowmore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_arrow_down,
                        0,
                        0,
                        0
                    )
                }
            })

            Glide.with(requireActivity())
                .load(it.imageurl)
                .circleCrop()
                .into(ImVDeceased)

        })


    }

    override fun getContent(item: Int): Fragment {
        when (item) {
            1 -> {
                return GalleryFragment()
            }
            2 -> {
                return CharityFragment()
            }
            else -> {
                return ForumFragment()
            }
        }
    }

    override fun getCount(): Int {
        return tabsTitle.size
    }


}