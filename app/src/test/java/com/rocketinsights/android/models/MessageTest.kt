package com.rocketinsights.android.models

import org.junit.Assert
import org.junit.Test

class MessageTest {

    @Test
    fun getText() {
        val message = Message("test")
        Assert.assertEquals("test", message.text)
    }

    @Test
    fun setText() {
        val message = Message()
        Assert.assertNull(message.text)
        message.text = "new test"
        Assert.assertEquals("new test", message.text)
    }
}