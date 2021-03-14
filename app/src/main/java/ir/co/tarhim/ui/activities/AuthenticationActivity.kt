package ir.co.tarhim.ui.activities


import android.content.Intent
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.ui.AbstractActivity

class AuthenticationActivity : AbstractActivity() {
    override val layoutId: Int
        get() = R.layout.activity_authentication
    override val navHostFragmentId: Int
        get() = R.id.authentication_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

}