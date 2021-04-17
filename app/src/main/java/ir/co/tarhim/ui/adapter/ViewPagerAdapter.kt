package ir.co.tarhim.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ir.co.tarhim.ui.callback.ViewPagerCallBack

class ViewPagerAdapter(
    fragment: FragmentManager,lifecycle: Lifecycle, var viewPagerCallBack: ViewPagerCallBack) :
    FragmentStateAdapter(fragment,lifecycle) {

    override fun getItemCount(): Int {
        return viewPagerCallBack.getCount()
    }

    override fun createFragment(position: Int): Fragment {
        return viewPagerCallBack.getContent(position)
    }
}