package ru.alexeyfedechkin.android.checkserver.enums

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