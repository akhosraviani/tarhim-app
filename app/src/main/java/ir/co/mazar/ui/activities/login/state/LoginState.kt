package ir.co.mazar.ui.activities.login.state

import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import ir.co.mazar.ui.activities.login.LoginActivity
import ir.co.mazar.utils.TarhimToast

abstract class LoginState (
    val activity: LoginActivity,
    val loginStartTv: AppCompatTextView,
    val loginEnterTv: AppCompatTextView,
    val loginEnterEt: AppCompatEditText,
    val btnSubmit: AppCompatButton,
    val changePasswordTv: AppCompatTextView,
    val helpTv: AppCompatTextView
) {
    private lateinit var mActivity: LoginActivity
    private lateinit var mLoginStartTv: AppCompatTextView
    private lateinit var mLoginEnterTv: AppCompatTextView
    private lateinit var mLoginEnterEt: AppCompatEditText
    private lateinit var mBtnSubmit: AppCompatButton
    private lateinit var mChangePasswordTv: AppCompatTextView
    private lateinit var mHelpTv: AppCompatTextView

    init {
        this.mActivity=activity
        this.mLoginStartTv=loginStartTv
        this.mLoginEnterTv=loginEnterTv
        this.mLoginEnterEt=loginEnterEt
        this.mBtnSubmit=btnSubmit
        this.mChangePasswordTv=changePasswordTv
        this.mHelpTv=helpTv
    }

    abstract fun decorateView()
    abstract fun submitBtnClickListener()

    public fun informUser(msg: String) {
        TarhimToast.Builder()
            .setActivity(activity)
            .message(msg)
            .build()
    }


}