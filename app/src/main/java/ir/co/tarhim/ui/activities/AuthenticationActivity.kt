package ir.co.tarhim.ui.activities


import ir.co.tarhim.R
import ir.co.tarhim.ui.AbstractActivity

class AuthenticationActivity : AbstractActivity() {
    override val layoutId: Int
        get() = R.layout.activity_authentication
    override val navHostFragmentId: Int
        get() =  R.id.authentication_fragment

}