package ir.co.tarhim.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment


abstract class AbstractActivity : AppCompatActivity() {

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    @get:LayoutRes
    protected abstract val layoutId: Int

    @get:IdRes
    protected abstract val navHostFragmentId: Int
    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        if (navHostFragmentId!=0) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(navHostFragmentId) as NavHostFragment

            navController = navHostFragment.navController
        }
    }


    fun display(function: () -> Unit) {
        runOnUiThread {
            function.invoke()
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

}