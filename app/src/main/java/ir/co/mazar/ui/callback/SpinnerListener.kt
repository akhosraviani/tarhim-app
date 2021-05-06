package ir.co.mazar.ui.callback

import android.widget.AdapterView

interface SpinnerListener {
        fun onNothing()
        fun onSelected(adapter: AdapterView<*>, position: Int)
    }