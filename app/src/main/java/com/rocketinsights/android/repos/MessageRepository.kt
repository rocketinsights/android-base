package com.rocketinsights.android.repos

import com.rocketinsights.android.network.ApiService

class MessageRepository(private val api: ApiService) {
    fun getMessageAsync() = api.getMessageAsync()
}