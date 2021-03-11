package com.rocketinsights.android.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiMessage(
    val uuid: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
