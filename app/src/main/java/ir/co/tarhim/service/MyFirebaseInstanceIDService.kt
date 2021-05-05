package ir.co.tarhim.service

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.utils.MazarAppJava



class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshToken: String = FirebaseInstanceId.getInstance().token.toString()
        Hawk.put("RefreshToken", refreshToken)
        val registrationComplete = Intent("firebase_service_registered")
        registrationComplete.putExtra("fcm_token", refreshToken)
        LocalBroadcastManager.getInstance(MazarAppJava.getContextApplication())
            .sendBroadcast(registrationComplete)
    }

    companion object {
        private val TAG = MyFirebaseInstanceIDService::class.java.simpleName
    }
}
