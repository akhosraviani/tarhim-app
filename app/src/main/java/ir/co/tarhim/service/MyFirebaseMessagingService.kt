package ir.co.tarhim.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.text.TextUtils
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.CommonNotificationBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.co.tarhim.ui.activities.home.HomeActivity
import ir.co.tarhim.ui.activities.login.LoginActivity
import android.content.Context;
import android.content.Context.ACCESSIBILITY_SERVICE
import androidx.annotation.NonNull;
import org.json.JSONException
import android.util.Log
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val challengeId = "tickupId"
    private val manager: NotificationManager? = null
    private val notificationBuilder: CommonNotificationBuilder? = null
    private val notificationChannel: NotificationChannel? = null
    private val challengeName = "tickup"
    private var notificationUtils: NotificationUtils? = null
  override  fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage == null) return

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e(
                TAG,
                "Notification Body: " + remoteMessage.notification!!.body
            )
            remoteMessage.notification!!.body?.let { handleNotification(it) }
        }

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(  TAG,
                "Data Payload: " + remoteMessage.data.toString())
            try {
                val json2 = JSONObject(remoteMessage.data.toString())
                handleDataMessage(json2)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }

    private fun handleNotification(message: String) {
        if (!NotificationUtils.isAppIsInBackground(applicationContext)) {
            // app is in foreground, broadcast the push message
            val pushNotification = Intent(this, LoginActivity::class.java)
            pushNotification.addFlags(Context.ACCESSIBILITY_SERVICE.toInt())
            pushNotification.putExtra("message", message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

            // play notification sound
            val notificationUtils = NotificationUtils(applicationContext)
            notificationUtils.playNotificationSound()
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")
        try {
            val data = json.getJSONObject("data")
            val title = data.getString("title")
            val message = data.getString("message")
            val isBackground = data.getBoolean("is_background")
            val imageUrl = data.getString("image")
            val timestamp = data.getString("timestamp")
            val payload = data.getJSONObject("payload")
            Log.e(TAG, "title: $title")
            Log.e(TAG, "message: $message")
            Log.e(TAG, "isBackground: $isBackground")
            Log.e(TAG, "payload: $payload")
            Log.e(TAG, "imageUrl: $imageUrl")
            Log.e(TAG, "timestamp: $timestamp")
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                val pushNotification = Intent("pushNotification")
                pushNotification.putExtra("message", message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

                // play notification sound
                val notificationUtils =
                    NotificationUtils(applicationContext)
                notificationUtils.playNotificationSound()
            } else {
                // app is in background, show the notification in notification tray
                val resultIntent = Intent(applicationContext, HomeActivity::class.java)
                resultIntent.putExtra("message", message)

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(
                        applicationContext,
                        title,
                        message,
                        timestamp,
                        resultIntent
                    )
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(
                        applicationContext,
                        title,
                        message,
                        timestamp,
                        resultIntent,
                        imageUrl
                    )
                }
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

    /**
     * Showing notification with text only
     */
    private fun showNotificationMessage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent
    ) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(packageName,title, message, timeStamp, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent,
        imageUrl: String
    ) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(packageName,title, message, timeStamp, intent, imageUrl)
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }
}
