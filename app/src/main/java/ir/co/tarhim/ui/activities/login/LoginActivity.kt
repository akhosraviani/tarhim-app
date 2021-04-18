package ir.co.tarhim.ui.activities.login


import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.toSpannable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.login.mobile.CheckPhoneNumberRequest
import ir.co.tarhim.ui.activities.home.HomeActivity
import ir.co.tarhim.ui.activities.login.state.InputOtpState
import ir.co.tarhim.ui.activities.login.state.InputPasswordState
import ir.co.tarhim.ui.activities.login.state.InputPhoneState
import ir.co.tarhim.ui.activities.login.state.LoginState
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.TarhimConfig
import ir.co.tarhim.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
        private set

    fun getViewModel(): HomeViewModel {
        return viewModel;
    }

    companion object {
        private const val CONDITION_WORDS_START_INDEX: Int = 32
        private const val CONDITION_WORDS_END_INDEX: Int = 48
        private const val RULES_WORDS_START_INDEX: Int = 77
        private const val RULES_WORDS_END_INDEX: Int = 97
    }

    private lateinit var state: LoginState
    private lateinit var inputPhoneState: InputPhoneState
    private lateinit var inputPasswordState: InputPasswordState
    private lateinit var otpInputState: InputOtpState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initUi()

        if(Hawk.get(TarhimConfig.FIRST_VISIT,false)){
            startActivity(Intent(this, HomeActivity::class.java))
        }

        viewModel.ldSignUp.observe(this, Observer { x ->
            showLoading(false)
            if (x.registered) {
                inputPhoneState.loginEnterEt.setText("")
                state = inputPasswordState
                inputPasswordState.serRegister(true)
                state.decorateView()
            } else {
                inputPhoneState.loginEnterEt.setText("")
                state = otpInputState
                state.decorateView()
                viewModel.requestOtp(CheckPhoneNumberRequest(loginEnterEt.text.toString()))

            }
        })

        viewModel.ldOtp.observe(this, Observer {
            showLoading(false)
            if (it.IsSuccessful) {
                TarhimToast.Builder()
                    .setActivity(this)
                    .message(it.Message)
                    .build()
            }
        })

        viewModel.ldConfirmOtp.observe(this, Observer {
            showLoading(false)
            when (it.code) {
                200 -> {
                    otpInputState.loginEnterEt.setText("")
                    state = inputPasswordState
                    state.decorateView()
                }
                400 -> {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message("کد شما صحیح نیست")
                        .build()
                }
            }
        })

        viewModel.ldSetPass.observe(this, Observer {
            showLoading(false)
            when (it.code) {
                200 -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }

            }

        })

        viewModel.ldConfirmPass.observe(this, Observer {
            showLoading(false)
            when (it.code) {
                200 -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                400 -> {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message(" رمز عبور نامعتبر میباشد")
                        .build()
                }
            }
        })


        changePassTv.setOnClickListener {
            inputPasswordState.loginEnterEt.setText("")
            state = otpInputState
            state.decorateView()
            viewModel.requestOtp(CheckPhoneNumberRequest(Hawk.get(TarhimConfig.USER_NUMBER)))

        }


    }


    private fun initUi() {
        inputPhoneState = InputPhoneState(
            this,
            loginStartTv,
            loginEnterTv,
            loginEnterEt,
            btnSubmitEnter,
            changePassTv,
            helpTv
        )
        inputPasswordState = InputPasswordState(
            this,
            loginStartTv,
            loginEnterTv,
            loginEnterEt,
            btnSubmitEnter,
            changePassTv,
            helpTv
        )
        otpInputState = InputOtpState(
            this,
            loginStartTv,
            loginEnterTv,
            loginEnterEt,
            btnSubmitEnter,
            changePassTv,
            helpTv
        )

        state = inputPhoneState
        state.decorateView()

        btnSubmitEnter.setOnClickListener {
            state.submitBtnClickListener()
            showLoading(true)
        }
        setHelpTv()

    }


    private fun setHelpTv() {

        val text = resources.getString(R.string.login_rules_description).toSpannable()

        val clickableSpanForCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                //go to condition's page
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }

        val clickableSpanForRules: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                //go to rule's page
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }

        val rulesWord = SpannableString(text)
        rulesWord.setSpan(
            clickableSpanForCondition,
            CONDITION_WORDS_START_INDEX,
            CONDITION_WORDS_END_INDEX,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        rulesWord.setSpan(
            clickableSpanForRules,
            RULES_WORDS_START_INDEX,
            RULES_WORDS_END_INDEX,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        helpTv.text = rulesWord
        helpTv.movementMethod = LinkMovementMethod.getInstance()
    }


    public fun showLoading(status: Boolean) {
        if (status) {
            loginLogo.visibility = View.INVISIBLE
            loading_lottie.visibility = View.VISIBLE
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            loginLogo.visibility = View.VISIBLE
            loading_lottie.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


}