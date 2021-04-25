package ir.co.tarhim.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import ir.co.tarhim.R
import ir.co.tarhim.ui.activities.inbox.InboxMessageActivity
import ir.co.tarhim.ui.adapter.ViewPagerAdapter
import ir.co.tarhim.ui.callback.ViewPagerCallBack
import ir.co.tarhim.ui.activities.deceased.CreateDeceasedActivity
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.OnBackPressed
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ViewPagerCallBack {

    private var tabs = listOf("ایجاد شده های من", "دنبال شده های من")
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        OnBackPressed().pressedCallBack(findNavController())
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.requestUserInfo()
        pagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, this)
        VPagerProfile.adapter = pagerAdapter


        viewModel.ldUserInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.name != null) {

                    TvuUserName.setText(it.name)
                    Glide.with(requireContext())
                        .load(it.imageurl)
                        .centerInside()
                        .circleCrop()
                        .into(ImVUser)
                }
            }
        })


        BtnNeedToPray.setOnClickListener {
            var args = bundleOf("PrayLink" to true)

            findNavController().navigate(R.id.action_fragment_profile_to_fragment_requirement, args)
        }
        TabLayoutMediator(tabProfile, VPagerProfile) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        for (i in 0 until tabProfile.tabCount) {
            val tab = (tabProfile.getChildAt(0) as ViewGroup).getChildAt(i)
            var p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 5, 0)
            tab.requestLayout()
        }


        BtnCreatePageDeceased.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateDeceasedActivity::class.java))
        }

        BtnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_profile_to_user_edit_fragment)
        }

        BtnNeedToPray.setOnClickListener {
            var args = bundleOf("PrayLink" to true)
            findNavController().navigate(R.id.action_fragment_profile_to_fragment_requirement, args)

        }

    }

    override fun getContent(item: Int): Fragment {
        when (item) {
            1 -> return MyFollowingFragment().newInstance(true)
            else -> return MyDeceasedFragment().newInstance(true)
        }

    }

    override fun getCount(): Int {
        return tabs.size
    }
}