package ir.co.tarhim.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.graphics.Color;
import android.net.Uri;
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat


import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.ui.activities.login.LoginActivity


class FirebaseMessageReceiver : FirebaseMessagingService() {
    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(
                TAG,
                "Message data payload: " + remoteMessage.data
            )
            if ( /* Check if data needs to be processed by long running job */true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                remoteMessage.notification?.body?.let {
                    scheduleJob(
                        it,
                        remoteMessage.notification?.title!!
                    )
                }
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
            // Check if message contains a notification payload.
            if (remoteMessage.notification != null) {
                Log.d( TAG,
                    "Message Notification Body: " + remoteMessage.notification!!.body
                )
                remoteMessage.notification!!.body?.let {
                    remoteMessage.notification!!.title?.let { it1 ->
                        sendNotification(
                            it,
                            it1
                        )
                    }
                }
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    //    private RemoteViews getCustomDesign(String title,
    //                                        String message) {
    //        RemoteViews remoteViews = new RemoteViews(
    //                getApplicationContext().getPackageName(),
    //                R.layout.notification);
    //        remoteViews.setTextViewText(R.id.title, title);
    //        remoteViews.setTextViewText(R.id.message, message);
    //        remoteViews.setImageViewResource(R.id.icon,
    //                R.drawable.new_log);
    //        return remoteViews;
    //    }
    //    private NotificationManager notificationManager;
    //    private NotificationCompat.Builder builder;
    //    public void showNotification(String title,
    //                                 String message) {
    //        // Pass the intent to switch to the MainActivity
    //        Intent intent
    //                = new Intent(this, LoginActivity.class);
    //        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    //        // Assign channel ID
    //        String channel_id = "notification_channel";
    //        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
    //        // the activities present in the activity stack,
    //        // on the top of the Activity that is to be launched
    //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //        // Pass the intent to PendingIntent to start the
    //        // next Activity
    //        PendingIntent pendingIntent
    //                = PendingIntent.getActivity(
    //                this, 0, intent,
    //                PendingIntent.FLAG_ONE_SHOT);
    //
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //            NotificationChannel channel = new NotificationChannel(channel_id, "name", NotificationManager.IMPORTANCE_HIGH);
    //            channel.setName(title);
    //            channel.setDescription(message);
    //            channel.setSound(null, null);
    //            channel.enableLights(true);
    //            channel.enableVibration(true);
    //            channel.setVibrationPattern(new long[]{1000, 1000, 1000,
    //                    1000, 1000});
    //            channel.notify();
    //            notificationManager.createNotificationChannel(channel);
    //        } else {
    //
    //            builder = new NotificationCompat
    //                    .Builder(getApplicationContext(), channel_id)
    //                    .setSmallIcon(R.drawable.new_log)
    //                    .setAutoCancel(true)
    //                    .setVibrate(new long[]{1000, 1000, 1000,
    //                            1000, 1000})
    //                    .setOnlyAlertOnce(true)
    //                    .setContentIntent(pendingIntent);
    //
    //            // A customized design for the notification can be
    //            // set only for Android versions 4.1 and above. Thus
    //            // condition for the same is checked here.
    //            if (Build.VERSION.SDK_INT
    //                    >= Build.VERSION_CODES.JELLY_BEAN) {
    //                builder = builder.setContent(
    //                        getCustomDesign(title, message));
    //            } // If Android Version is lower than Jelly Beans,
    //            // customized layout cannot be used and thus the
    //            // layout is set as follows
    //            else {
    //                builder = builder.setContentTitle(title)
    //                        .setContentText(message)
    //                        .setSmallIcon(R.drawable.new_log);
    //            }
    //            // Create an object of NotificationManager class to
    //            // notify the
    //            // user of events that happen in the background.
    //            NotificationManager notificationManager
    //                    = (NotificationManager) getSystemService(
    //                    Context.NOTIFICATION_SERVICE);
    //            // Check if the Android Version is greater than Oreo
    //
    //            notificationManager.notify(0, builder.build());
    //        }
    //    }
   override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    // [END on_new_token]
    private fun scheduleJob(body: String, title: String) {
        sendNotification(body, title)
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String) {
//        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener({ v -> })
        Hawk.put("RefreshToken", token)
    }

//    private var notificationBuilder: NotificationCompat.Builder? = null
    private var manager: NotificationManager? = null
    private fun sendNotification(messageBody: String, title: String) {
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            "channelId"
        )
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationLayout = RemoteViews(packageName, R.layout.notification_small)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        notificationLayout.setTextViewText(R.id.notifTitle,title)
        notificationLayout.setImageViewResource(R.id.notifIcon , R.drawable.more_icon)
        notificationBuilder
//            .setSmallIcon(R.drawable.more_icon)
//            .setContentTitle(title)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            manager!!.createNotificationChannel(channel)
        }
        manager!!.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FirebaseMessageReceiver"
    }
}
