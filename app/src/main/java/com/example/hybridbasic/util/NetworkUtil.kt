package com.example.hybridbasic.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

fun getIpAddress(context: Context): String? {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = cm.activeNetworkInfo

    return when (networkInfo?.type) {
        ConnectivityManager.TYPE_WIFI -> {
            val wm = context.getSystemService(Context.WIFI_SERVICE) as? WifiManager ?: return null
            val idAddress = wm.connectionInfo.ipAddress
            String.format(
                "%d.%d.%d.%d",
                idAddress and 0xff,
                idAddress shr 8 and 0xff,
                idAddress shr 16 and 0xff,
                idAddress shr 24 and 0xff
            )
        }
        ConnectivityManager.TYPE_MOBILE -> getIp4Address()
        else -> null
    }
}


private fun getIp4Address(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.hostAddress
                }
            }
        }
    } catch (e: SocketException) {
        e.printStackTrace()
    }
    return null
}