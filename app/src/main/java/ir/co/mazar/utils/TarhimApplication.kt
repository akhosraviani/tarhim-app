package ir.co.mazar.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import ir.co.mazar.network.RequestClient

class TarhimApplication : Application() {

    open fun getContextApplication(): TarhimApplication? {
        return context
    }


    private val androidId: String? = null
    private val context: TarhimApplication? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        RequestClient.initRetrofit(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        FirebaseApp.getInstance()
        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task: Task<Void?> ->
                if (!task.isSuccessful) {
                }
            }


    }

}