package ir.co.tarhim.ui.fragments.authentication


import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.text.toSpannable
import ir.co.tarhim.R
import ir.co.tarhim.ui.AbstractFragment
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : AbstractFragment() {

    companion object {
        private const val CONDITION_WORDS_START_INDEX: Int = 32
        private const val CONDITION_WORDS_END_INDEX: Int = 46
        private const val RULES_WORDS_START_INDEX: Int = 80
        private const val RULES_WORDS_END_INDEX: Int = 97
    }

    override val layoutId: Int
        get() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginCv.setBackgroundResource(R.drawable.shape_login_card_view_border)

        val text = resources.getString(R.string.login_rules_description).toSpannable()

        val clickableSpanForCondition:ClickableSpan = object :ClickableSpan(){
            override fun onClick(p0: View) {
                //go to condition's page
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText=true
            }

        }

        val clickableSpanForRules:ClickableSpan = object :ClickableSpan(){
            override fun onClick(p0: View) {
                //go to rule's page
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText=true
            }

        }

        val rulesWord = SpannableString(text)
        rulesWord.setSpan(clickableSpanForCondition, CONDITION_WORDS_START_INDEX, CONDITION_WORDS_END_INDEX, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        rulesWord.setSpan(clickableSpanForRules, RULES_WORDS_START_INDEX, RULES_WORDS_END_INDEX, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginRulesTv.text = rulesWord
        loginRulesTv.movementMethod = LinkMovementMethod.getInstance()
    }


}