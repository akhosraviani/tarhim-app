package ir.co.tarhim.ui.activities.login.state

import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.login.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.ui.activities.login.LoginActivity
import ir.co.tarhim.utils.TarhimConfig.Companion.USER_NUMBER

class InputOtpState(
    activity: LoginActivity,
    loginStartTv: AppCompatTextView,
    loginEnterTv: AppCompatTextView,
    loginEnterEt: AppCompatEditText,
    btnSubmit: AppCompatButton,
    changePasswordTv: AppCompatTextView,
    helpTv: AppCompatTextView
) : LoginState(
    activity,
    loginStartTv,
    loginEnterTv,
    loginEnterEt,
    btnSubmit,
    changePasswordTv,
    helpTv
) {

    override fun decorateView() {
        helpTv.visibility=View.GONE
        loginEnterEt.setHint("*****")
        loginStartTv.setText(activity.getString(R.string.verification_title))
        loginEnterTv.setText(activity.getString(R.string.verification_start_description))
        loginEnterEt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(5))
        changePasswordTv.visibility=View.INVISIBLE
    }

    override fun submitBtnClickListener() {

        if (TextUtils.isEmpty(loginEnterEt.text)) {
            informUser("لطفا کد ارسال شده را وارد کنید")
        }
        activity.getViewModel().requestConfirmOtp(
            ConfirmOtpRequest(
                loginEnterEt.text.toString(),
                Hawk.get(USER_NUMBER)
            )
        )
    }
}