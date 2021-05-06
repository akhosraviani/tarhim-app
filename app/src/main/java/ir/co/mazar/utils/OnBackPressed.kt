package ir.co.mazar.utils

import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import ir.co.mazar.R

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