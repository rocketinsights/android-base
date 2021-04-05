package com.rocketinsights.android.auth

import kotlinx.coroutines.flow.Flow

/**
 * This interface should be implemented by the concrete class that handle anything
 * related to the user's token and session.
 */
interface SessionWatcher {
    suspend fun isSignedIn(): Boolean

    suspend fun refreshAccessToken(): String?

    suspend fun getAccessToken(): String?

    fun observeSessionCleared(): Flow<Boolean>

    suspend fun signOut()
}
