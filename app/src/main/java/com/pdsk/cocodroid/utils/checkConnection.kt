package com.pdsk.cocodroid.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log

fun isOnline(context: Context?):Boolean{
    if(context == null) return false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

    if(capabilities != null){
        if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
            Log.i("Internet","NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
            Log.i("Internet","NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
            Log.i("Internet","NetworkCapabilities.TRANSPORT_WIFI")
            return true
        }
    }

    return false
}