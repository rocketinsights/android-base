package com.rocketinsights.android.models

data class Message(
    val id: Long = 0L,
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
