package ir.co.mazar.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        networkListener = context as NetworkListener

        when (isNetworkConnected(context!!)) {
            true -> networkListener.networkcallback(true)
            else -> networkListener.networkcallback(false)
        }
    }


    private fun isNetworkConnected(context: Context): Boolean {

        var manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val nm = manager.activeNetwork ?: return false
                val actNw = manager.getNetworkCapabilities(nm) ?: return false
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            else -> {
                val netInfo = manager.activeNetworkInfo ?: return false
                netInfo.isConnected
            }
        }
    }


    interface NetworkListener {
        fun networkcallback(isConnected: Boolean)
    }

    private lateinit var networkListener: NetworkListener


}