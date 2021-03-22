package ir.co.tarhim.ui.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

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
        changeBottomIcon(
            bottom_navigation.menu,
            bottom_navigation.menu.findItem(bottom_navigation.selectedItemId),
            getEnableSelectedIcon().get(R.id.fragment_cemetery)
        )

        Log.e(TAG, "onCreate UserNumber: "+Hawk.get("UserNumber") )

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            changeBottomIcon(bottom_navigation.menu, item, getEnableSelectedIcon().get(item.itemId))
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
    fun changeBottomIcon(menu: Menu, menuItem: MenuItem, focusedItemDrwable: Int) {
        menu.getItem(0).setIcon(R.drawable.profil_icon)
        menu.getItem(1).setIcon(R.drawable.news_icon)
        menu.getItem(2).setIcon(R.drawable.behesht_icon)
        menu.getItem(3).setIcon(R.drawable.niazmandiha_icon)
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

}

