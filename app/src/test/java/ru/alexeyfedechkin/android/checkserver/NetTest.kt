package ru.alexeyfedechkin.android.checkserver

import org.junit.Test

import org.junit.Assert.*
import ru.alexeyfedechkin.android.checkserver.enums.Protocol
import ru.alexeyfedechkin.android.checkserver.enums.ServerStatus
import ru.alexeyfedechkin.android.checkserver.models.Server
import ru.alexeyfedechkin.android.checkserver.network.Net

class NetTest {

    @Test
    fun checkServerStatus() {
        val server = Server()
        server.port = 443
        server.hostname = "vk.com"
        server.protocol = Protocol.HTTPS
        server.responseCode = 200
        assertEquals(
            ServerStatus.ONLINE,
            Net.checkServerStatus(server))
        server.hostname = "asasdfasdfttff"
        assertEquals(
            ServerStatus.OFFLINE,
            Net.checkServerStatus(server))
        server.hostname = "vk.com"
        server.responseCode = 404
        assertEquals(
            ServerStatus.INVALID_RESPONSE_CODE,
            Net.checkServerStatus(server))
    }
}