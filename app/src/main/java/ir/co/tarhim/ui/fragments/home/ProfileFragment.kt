package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ir.co.tarhim.R
import ir.co.tarhim.ui.adapter.ViewPagerAdapter
import ir.co.tarhim.ui.callback.ViewPagerCallBack
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ViewPagerCallBack {

    private var tabs = listOf("ایجاد شده های من", "دنبال شده های من")
    private lateinit var pagerAdapter: ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = ViewPagerAdapter(this, this)
        VPagerProfile.adapter = pagerAdapter

        TabLayoutMediator(tabProfile, VPagerProfile) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }

    override fun getContent(item: Int): Fragment {
        when (item) {
            1 -> return FollowDeceasedFragment().newInstance(true)
            else -> return MyDeceasedFragment().newInstance(true)
        }

    }

    override fun getCount(): Int {
        return tabs.size
    }
}