package com.rocketinsights.android.prefs

import kotlinx.coroutines.flow.Flow

interface AuthLocalStore {

    fun getAuthToken(): Flow<String>

    suspend fun setAuthToken(token: String)
}
