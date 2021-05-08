package ir.co.mazar.ui.activities.login.state

import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doOnTextChanged
import com.orhanobut.hawk.Hawk
import ir.co.mazar.R
import ir.co.mazar.model.login.confirmpass.ConfirmPasswordRequest
import ir.co.mazar.ui.activities.login.LoginActivity
import ir.co.mazar.utils.TarhimConfig.Companion.USER_NUMBER

class InputPasswordState(
    activity: LoginActivity,
    loginStartTv: AppCompatTextView,
    loginEnterTv: AppCompatTextView,
    loginEnterEt: AppCompatEditText,
    btnSubmit: AppCompatButton,
    changePasswordTv: AppCompatTextView,
    helpTv: AppCompatTextView
) : LoginState(activity, loginStartTv, loginEnterTv, loginEnterEt, btnSubmit, changePasswordTv,helpTv) {


    public var register: Boolean = false
        public set

    fun serRegister(reg:Boolean){
        this.register=reg
    }

    override fun decorateView() {
        loginEnterEt.setHint("")
        loginEnterEt.inputType=InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
        helpTv.visibility= View.GONE
        loginEnterEt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        loginStartTv.setText(activity.getString(R.string.signIn_title))
        loginEnterTv.setText(activity.getString(R.string.signIn_start_description))
        loginEnterEt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        loginEnterEt.doOnTextChanged { _, _, _, _ ->
            run {
                if (TextUtils.isEmpty(loginEnterEt.text)) {
                    loginEnterEt.setBackgroundResource(R.drawable.shape_login_edit_text_phone_number)
                } else {
                    loginEnterEt.setBackgroundResource(R.drawable.shape_purple_card)
                }
            }
        }
        changePasswordTv.setText(activity.getString(R.string.signIn_password_recovery))
        btnSubmit.setText(activity.getString(R.string.signIn_continue))
    }

    override fun submitBtnClickListener() {

        if (TextUtils.isEmpty(loginEnterEt.text)) {
            informUser("لطفا رمز عبور خود را وارد کنید")

        }

        if(!register){
        activity.getViewModel().requestConfirmPassword(
            ConfirmPasswordRequest(
                Hawk.get(USER_NUMBER),
                loginEnterEt.text.toString()))
        } else{
            activity.getViewModel().requestSetPassword(
                ConfirmPasswordRequest(
                    Hawk.get(USER_NUMBER),
                    loginEnterEt.text.toString()))
        }

    }


}