package ru.alexeyfedechkin.android.checkserver.network

import ru.alexeyfedechkin.android.checkserver.models.Server
import ru.alexeyfedechkin.android.checkserver.enums.ServerStatus
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
            val url = buildUrl(server)
            if (ping(url) == server.responseCode){
                ServerStatus.ONLINE
            } else {
                ServerStatus.INVALID_RESPONSE_CODE
            }
        } catch (ex: Throwable){
            ServerStatus.OFFLINE
        }
    }

    /**
     * TODO
     *
     * @param server
     * @return
     */
    fun checkServerResponse(server: Server):Int{
        return try {
            ping(buildUrl(server))
        }  catch (ex:Throwable){
            return -1
        }
    }

    private fun ping(url:String): Int {
        val uri = URL(url)
        val httpUrlConnection = uri.openConnection() as HttpURLConnection
        httpUrlConnection.setRequestProperty("User-Agent", "Android Application")
        httpUrlConnection.setRequestProperty("Connection", "close")
        httpUrlConnection.connectTimeout = 1000
        httpUrlConnection.connect()
        return httpUrlConnection.responseCode
    }

    private fun buildUrl(server: Server):String{
        return "${server.protocol.protocol}://${server.hostname}:${server.port}"
    }
}