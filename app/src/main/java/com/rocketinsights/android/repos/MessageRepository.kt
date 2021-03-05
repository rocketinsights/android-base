package com.rocketinsights.android.repos

import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.network.ApiService
import kotlinx.coroutines.withContext

class MessageRepository(private val api: ApiService, private val dispatcher: DispatcherProvider) {
    suspend fun getMessage(): Message = withContext(dispatcher.io()) { api.getMessage() }
}