package ir.co.tarhim.ui.callback

import android.widget.AdapterView

interface SpinnerListener {
        fun onNothing()
        fun onSelected(adapter: AdapterView<*>, position: Int)
    }