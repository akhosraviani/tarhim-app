package ir.co.tarhim.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.orhanobut.hawk.Hawk

class TarhimApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}