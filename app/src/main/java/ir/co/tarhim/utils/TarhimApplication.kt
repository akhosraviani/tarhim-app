package ir.co.tarhim.utils

import android.app.Application
import com.orhanobut.hawk.Hawk

class TarhimApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this)
    }
}