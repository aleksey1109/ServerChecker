package ru.alexeyfedechkin.android.checkserver.Network

import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.ServerStatus
import java.net.HttpURLConnection
import java.net.URL

object Net {

    /**
     * TODO
     *
     * @param axpectedResponseCode
     * @param hostname
     * @return
     */
    fun checkServerStatus(axpectedResponseCode:Int, hostname:String): ServerStatus {
        try {
            val url = URL(hostname)
            val httpUrlConnection = url.openConnection() as HttpURLConnection
            httpUrlConnection.setRequestProperty("User-Agent", "Android Application")
            httpUrlConnection.setRequestProperty("Connection", "close")
            httpUrlConnection.connectTimeout = 1000 * 30
            httpUrlConnection.connect()
            return if (httpUrlConnection.responseCode == axpectedResponseCode){
                ServerStatus.ONLINE
            } else {
                ServerStatus.INVALID_RESPONSE_CODE
            }
        } catch (ex: Throwable){
            return ServerStatus.OFFLINE
        }
    }

    fun checkStatus(server:Server): ServerStatus {
        return checkServerStatus(
            server.responseCode,
            server.hostname
        )
    }
}