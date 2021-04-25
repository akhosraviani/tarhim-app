package ir.co.tarhim.ui.activities.home

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.ui.fragments.profile.EditProfileFragment
import ir.co.tarhim.utils.NetworkConnectionReceiver
import ir.co.tarhim.utils.TarhimConfig.Companion.FIRST_VISIT
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.concurrent.schedule


class HomeActivity : AppCompatActivity(), NetworkConnectionReceiver.NetworkListener {

    companion object {
        private const val TAG = "HomeActivity"
    }

    private lateinit var navController: NavController
    private var TIME_INTERVAL: Long = 2000
    private var mBackPressed: Long? = 0
    val br: BroadcastReceiver = NetworkConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(br, filter)

        setContentView(R.layout.activity_home)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottom_navigation, navController)
//        if (!Hawk.get(FIRST_VISIT, false)){
//           homePageRoot.visibility=View.GONE
//           profile_sheet.visibility=View.VISIBLE
//            supportFragmentManager.beginTransaction()
//                .add(R.id.profile_sheet,EditProfileFragment())
//                .commit()
//        }

        bottom_navigation.setOnNavigationItemReselectedListener { }
        bottom_navigation.itemIconTintList = null
        changeBottomIcon(
            bottom_navigation.menu,
            bottom_navigation.menu.findItem(bottom_navigation.selectedItemId),
            getEnableSelectedIcon().get(R.id.fragment_cemetery)
        )

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            changeBottomIcon(bottom_navigation.menu, item, getEnableSelectedIcon().get(item.itemId))
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
    fun changeBottomIcon(menu: Menu, menuItem: MenuItem, focusedItemDrwable: Int) {
        menu.getItem(0).setIcon(R.drawable.behesht_icon)
        menu.getItem(1).setIcon(R.drawable.news_icon)
        menu.getItem(2).setIcon(R.drawable.niazmandiha_icon)
        menu.getItem(3).setIcon(R.drawable.profil_icon)
        menuItem.setChecked(false)
        menuItem.setIcon(focusedItemDrwable)
        navController.navigate(menuItem.itemId)
    }

    private fun getEnableSelectedIcon(): SparseIntArray {
        var sparseIcon = SparseIntArray()
        sparseIcon.put(R.id.fragment_profile, R.drawable.profil_icon_selected)
        sparseIcon.put(R.id.fragment_news, R.drawable.news_icon_selected)
        sparseIcon.put(R.id.fragment_cemetery, R.drawable.behesht_icon_selected)
        sparseIcon.put(R.id.fragment_requirement, R.drawable.niazmandiha_icon_selected)

        return sparseIcon
    }

    override fun onBackPressed() {

        if (!navController.popBackStack()) {
            if (mBackPressed!! + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity()
            } else {
                Toast.makeText(this, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT).show()
            }

            mBackPressed = System.currentTimeMillis()
        }
            navController.popBackStack()
            changeBottomIcon(
                bottom_navigation.menu,
                bottom_navigation.menu.findItem(bottom_navigation.selectedItemId),
                getEnableSelectedIcon()
                    .get(bottom_navigation.selectedItemId)
            )

    }

    override fun networkcallback(isConnected: Boolean) {
        if (isConnected) {
            homePageRoot.visibility = View.VISIBLE
            noIntenterroot.visibility = View.GONE

        } else {
            homePageRoot.visibility = View.GONE
            noIntenterroot.visibility = View.VISIBLE
            Timer("Network", false).schedule(4000) {
                finishAffinity()
            }
        }
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(br)
    }

}

