package ru.alexeyfedechkin.android.checkserver.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import ru.alexeyfedechkin.android.checkserver.models.Server
import ru.alexeyfedechkin.android.checkserver.enums.ServerStatus
import java.net.HttpURLConnection
import java.net.URL

/**
 * Network Interaction
 */
object Net {

    /**
     * check internet connection
     * @param context application context
     * @return true if network connection amiable
     */
    private fun isOnline(context: Context): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info)
            if (i.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        return false
    }

    /**
     * get server status
     * @see ServerStatus
     * @return status of server
     */
    fun checkServerStatus(server: Server, context: Context): ServerStatus {
        if (!isOnline(context)){
            return ServerStatus.NO_INTERNET_ACCESS
        }
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
     * get server response
     * @param server instance of server
     * @return http response code
     */
    fun checkServerResponse(server: Server):Int{
        return try {
            ping(buildUrl(server))
        }  catch (ex:Throwable){
            return -1
        }
    }

    /**
     * check server response
     * @param url server url
     * @return http response code
     */
    private fun ping(url:String): Int {
        val uri = URL(url)
        val httpUrlConnection = uri.openConnection() as HttpURLConnection
        httpUrlConnection.setRequestProperty("User-Agent", "Android CheckServerApplication")
        httpUrlConnection.setRequestProperty("Connection", "close")
        httpUrlConnection.connectTimeout = 1000
        httpUrlConnection.connect()
        return httpUrlConnection.responseCode
    }

    /**
     * build url from server
     * example: http://axemple.org:80
     * @param server instance of server
     * @return string with url
     */
    private fun buildUrl(server: Server):String{
        return "${server.protocol.protocol}://${server.hostname}:${server.port}"
    }
}