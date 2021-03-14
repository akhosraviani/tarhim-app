package ir.co.tarhim.ui.callback

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

interface ViewPagerCallBack {
    fun getContent(item: Int): Fragment
    fun getCount(): Int
}