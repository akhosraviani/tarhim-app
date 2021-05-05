package ir.co.tarhim.service

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.ui.activities.home.HomeActivity


class FirebaseMessageServiceBackground : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
    }

   override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...


//        Toast.makeText(this, "message come"+remoteMessage.getMessageId(), Toast.LENGTH_SHORT).show();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG,
                "Message data payload: " + remoteMessage.data
            )
            if (remoteMessage.data.containsKey("live")) {
                val in2 = Intent(this, HomeActivity::class.java)
                in2.putExtra("live", remoteMessage.data["live"])
                in2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(in2)
            }
            if ( /* Check if data needs to be processed by long running job */true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private fun handleNow() {}
    private fun scheduleJob() {}

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Hawk.put("RefreshToken", token)
    }

    companion object {
        private const val TAG = "FirebaseNotificationSer"
    }
}
