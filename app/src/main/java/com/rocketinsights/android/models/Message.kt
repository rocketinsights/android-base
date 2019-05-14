package com.rocketinsights.android.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(var text: String? = null)