package ir.co.mazar.utils

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ir.co.mazar.ui.callback.SpinnerListener

class SpinnerTarhim {

    public fun initSpinner(
        callback: SpinnerListener,
        activity: Activity,
        layoutId: Int,
        spinner: Spinner,
        itemList: Array<String>
    ) {

        val aAdapter = ArrayAdapter(activity, layoutId, itemList)
        spinner.adapter = aAdapter
        aAdapter.setDropDownViewResource(layoutId)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                callback.onSelected(p0!!, p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                callback.onNothing()
            }

        }
    }

//    fun initSpinner(callback: DeceasedPageFragment, activity: FragmentActivity, layoutId: Int, spinner: AppCompatTextView?, itemList: Array<String>) {
//
//    }


}