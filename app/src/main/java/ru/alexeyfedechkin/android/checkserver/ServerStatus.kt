package ru.alexeyfedechkin.android.checkserver

/**
 * enumeration of server status
 */
enum class ServerStatus {
    /**
     * server online and returned expected http code
     */
    ONLINE,
    /**
     * server offline
     */
    OFFLINE,
    /**
     * server online but returned unexpected http code
     */
    INVALID_RESPONSE_CODE
}