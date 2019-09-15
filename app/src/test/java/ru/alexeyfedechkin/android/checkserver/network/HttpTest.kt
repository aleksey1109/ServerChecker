package ru.alexeyfedechkin.android.checkserver.network

import org.junit.Test

import org.junit.Assert.*

class HttpTest {

    @Test
    fun validateResponseCode() {
        assertEquals(true, Http.validateResponseCode(200))
        assertEquals(false, Http.validateResponseCode(600))
    }

    @Test
    fun validatePort(){
        assertEquals(true, Http.validatePort(100))
        assertEquals(false, Http.validatePort(-1))
        assertEquals(false, Http.validatePort(65536))
    }
}