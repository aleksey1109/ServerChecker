package ru.alexeyfedechkin.android.checkserver.Network

import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.ServerStatus
import java.net.HttpURLConnection
import java.net.URL

/**
 * Network Interaction
 */
object Net {

    /**
     * get server status
     * @see ServerStatus
     * @param expectedResponseCode http code that expected from server
     * @param hostname server url
     * @return
     */
    fun checkServerStatus(expectedResponseCode:Int, hostname:String): ServerStatus {
        return try {
            val url = URL(hostname)
            val httpUrlConnection = url.openConnection() as HttpURLConnection
            httpUrlConnection.setRequestProperty("User-Agent", "Android Application")
            httpUrlConnection.setRequestProperty("Connection", "close")
            httpUrlConnection.connectTimeout = 1000 * 30
            httpUrlConnection.connect()
            if (httpUrlConnection.responseCode == expectedResponseCode){
                ServerStatus.ONLINE
            } else {
                ServerStatus.INVALID_RESPONSE_CODE
            }
        } catch (ex: Throwable){
            ServerStatus.OFFLINE
        }
    }

    /**
     * check server status by giving server instance
     * @param server instance of server for which you need to check server status
     * @return ServerStatus
     */
    fun checkStatus(server:Server): ServerStatus {
        return checkServerStatus(
            server.responseCode,
            server.hostname
        )
    }
}