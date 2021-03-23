package com.rocketinsights.android.auth

import kotlinx.coroutines.flow.Flow

/**
 * This class should be the means to handle anything related to the user's token.
 */
interface SessionWatcher {
    suspend fun isSignedIn(): Boolean

    suspend fun refreshAccessToken(): String?

    suspend fun getAccessToken(): String?

    fun observeSessionCleared(): Flow<Boolean>

    suspend fun signOut()
}
