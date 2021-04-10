package ir.co.tarhim.ui.activities.login

import android.app.Activity
import android.icu.util.TimeZone
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import ir.co.tarhim.utils.TarhimToast

abstract class LoginState(
    val activity: Activity,
    val editText: AppCompatEditText,
    val btnSubmit: AppCompatButton,
    val sendOtp: AppCompatTextView
) {


    abstract fun decorateView()
    abstract fun submitBtn()

    public fun informUser(msg:String) {
        TarhimToast.Builder()
            .setActivity(activity)
            .message(msg)
            .build()

    }

}