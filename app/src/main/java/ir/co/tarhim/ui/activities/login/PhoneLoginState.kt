package ir.co.tarhim.ui.activities.login

import android.app.Activity
import android.text.InputFilter
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView


class PhoneLoginState(
    activity: Activity,
    editText: AppCompatEditText,
    btnSubmit: AppCompatButton,
    sendOtp: AppCompatTextView
) : LoginState(activity, editText, btnSubmit, sendOtp) {

    override fun decorateView() {
        editText.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetterOrDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        })
    }

    override fun submitBtn() {
        if (editText.text!!.length == 0) {
            informUser("شماره خود را وارد کنید")
        }
        if (editText.text!!.length < 11) {
            informUser("شماره خود را به درستی وارد کنید")
        }

    }
}