package ru.alexeyfedechkin.android.checkserver.Network

import ru.alexeyfedechkin.android.checkserver.Models.Server
import ru.alexeyfedechkin.android.checkserver.Enums.ServerStatus
import java.net.HttpURLConnection
import java.net.URL

/**
 * Network Interaction
 */
object Net {

    /**
     * get server status
     * @see ServerStatus
     * @return
     */
    fun checkServerStatus(server: Server): ServerStatus {
        return try {
            val url = "${server.protocol.protocol}://${server.hostname}:${server.port}"
            val uri = URL(url)
            val httpUrlConnection = uri.openConnection() as HttpURLConnection
            httpUrlConnection.setRequestProperty("User-Agent", "Android Application")
            httpUrlConnection.setRequestProperty("Connection", "close")
            httpUrlConnection.connectTimeout = 1000 * 30
            httpUrlConnection.connect()
            if (httpUrlConnection.responseCode == server.responseCode){
                ServerStatus.ONLINE
            } else {
                ServerStatus.INVALID_RESPONSE_CODE
            }
        } catch (ex: Throwable){
            ServerStatus.OFFLINE
        }
    }
}