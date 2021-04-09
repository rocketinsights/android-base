package com.rocketinsights.android.models

import org.junit.Assert.assertEquals
import org.junit.Test

class MessageTest {

    private val id = 1L
    private val text = "test"
    private val timestamp = 1234567L

    @Test
    fun testProperties() {
        val message = Message(id = id, text = text, timestamp = timestamp)
        assertEquals(id, message.id)
        assertEquals(text, message.text)
        assertEquals(timestamp, message.timestamp)
    }
}
