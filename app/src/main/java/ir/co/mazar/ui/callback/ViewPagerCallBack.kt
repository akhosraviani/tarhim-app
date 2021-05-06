package ir.co.mazar.ui.callback

import androidx.fragment.app.Fragment

interface ViewPagerCallBack {
    fun getContent(item: Int): Fragment
    fun getCount(): Int
}