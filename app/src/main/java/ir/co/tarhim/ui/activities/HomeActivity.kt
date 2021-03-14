package ir.co.tarhim.ui.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "HomeActivity"
    }

    var doubleBackToExitPressedOnce = false
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottom_navigation, navController)
        bottom_navigation.setOnNavigationItemReselectedListener { }
        bottom_navigation.itemIconTintList = null

        Log.e(TAG, "onCreate: UserNumber" + Hawk.get("UserNumber"))

    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()



}

