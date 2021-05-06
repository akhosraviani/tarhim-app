package ir.co.mazar.sevice

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.orhanobut.hawk.Hawk
import ir.co.mazar.utils.TarhimApplication

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    private val TAG = MyFirebaseInstanceIDService::class.java.simpleName

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshToken = FirebaseInstanceId.getInstance().token
        Hawk.put("RefreshToken", refreshToken)
        val registrationComplete = Intent("firebase_service_registered")
        registrationComplete.putExtra("fcm_token", refreshToken)
        TarhimApplication().getContextApplication()?.let {
            LocalBroadcastManager.getInstance(it)
                .sendBroadcast(registrationComplete)
        }
    }
}