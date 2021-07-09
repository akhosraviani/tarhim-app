package ir.co.mazar.ui.activities.login.state

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.orhanobut.hawk.Hawk
import ir.co.mazar.R
import ir.co.mazar.model.login.mobile.CheckPhoneNumberRequest
import ir.co.mazar.ui.activities.login.LoginActivity
import ir.co.mazar.utils.TarhimConfig.Companion.USER_NUMBER


class InputPhoneState(
    activity: LoginActivity,
    loginStartTv: AppCompatTextView,
    loginEnterTv: AppCompatTextView,
    loginEnterEt: AppCompatEditText,
    btnSubmit: AppCompatButton,
    changePasswordTv: AppCompatTextView,
    helpTv: AppCompatTextView
) : LoginState(activity, loginStartTv, loginEnterTv, loginEnterEt, btnSubmit, changePasswordTv,helpTv) {



    override fun submitBtnClickListener() {

        if (TextUtils.isEmpty(loginEnterEt.text)) {
            informUser("شماره موبایل خود را وراد کنید")
            return
        }
        if (loginEnterEt.text!!.length < 11 || !loginEnterEt.text!!.startsWith("09")) {
            informUser("لطفا شماره خود را به صورت صحیح وارد کنید")
            return
        }

        activity.getViewModel()
            .requestCheckRegister(CheckPhoneNumberRequest(loginEnterEt.text.toString()))
        Hawk.put(USER_NUMBER, loginEnterEt.text.toString())
        activity.showLoading(true)
    }

    override fun decorateView() {
        loginStartTv.setText(activity.getString(R.string.login_start))
        loginEnterTv.setText(activity.getString(R.string.login_start_description))
        loginEnterEt.filters = arrayOf<InputFilter>(LengthFilter(11))
        loginEnterEt.setHint("09*********")
        loginEnterEt.inputType= InputType.TYPE_CLASS_NUMBER
        btnSubmit.setText(activity.getString(R.string.login_start_button))
        helpTv.visibility = View.VISIBLE
    }
}