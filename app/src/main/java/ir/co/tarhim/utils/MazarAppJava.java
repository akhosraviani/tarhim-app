package ir.co.tarhim.utils;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.provider.Settings;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.hawk.Hawk;

public class MazarAppJava extends Application {

    private static String androidId;
    private static MazarAppJava context;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static MazarAppJava getContextApplication() {
        return context;
    }

    private static final String TAG = "TickupApplication";

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Hawk.init(this).build();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

            }
        });
        FirebaseCrashlytics.getInstance();

        FirebaseApp.getInstance();
//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Log.e(TAG, "onComplete: " + msg);
//                    }
//                });


        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public float checkVersionCode(Context context) {
        int versionCode = 0;
        String versionName = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] vNamearray=versionName.split("-");
        float version=Float.parseFloat(versionCode+"."+vNamearray[0]);
        return version;
    }

    public static String getAndroidId() {
        return androidId;
    }
}

