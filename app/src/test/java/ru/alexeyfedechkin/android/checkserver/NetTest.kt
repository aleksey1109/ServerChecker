package ru.alexeyfedechkin.android.checkserver

import org.junit.Test

import org.junit.Assert.*
import ru.alexeyfedechkin.android.checkserver.Network.Net

class NetTest {

    @Test
    fun checkServerStatus() {
        assertEquals(ServerStatus.ONLINE,
            Net.checkServerStatus(200, "https://vk.com"))
        assertEquals(ServerStatus.OFFLINE,
            Net.checkServerStatus(200, "https://asasdfasdfttff"))
        assertEquals(ServerStatus.INVALID_RESPONSE_CODE,
            Net.checkServerStatus(404, "https://vk.com"))
    }
}