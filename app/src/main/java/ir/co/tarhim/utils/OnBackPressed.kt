package ir.co.tarhim.utils

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ir.co.tarhim.R

class OnBackPressed {
    fun pressedCallBack(controller:NavController) {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    controller.popBackStack(R.id.fragment_cemetery,false)
                }
            }

    }


}