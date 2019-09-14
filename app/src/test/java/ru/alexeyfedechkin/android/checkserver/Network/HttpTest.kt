package ru.alexeyfedechkin.android.checkserver.Network

import org.junit.Test

import org.junit.Assert.*

class HttpTest {

    @Test
    fun validateResponseCode() {
        assertEquals(true, Http.validateResponseCode(200))
        assertEquals(false, Http.validateResponseCode(600))
    }
}