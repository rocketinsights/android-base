package com.rocketinsights.android.auth

/**
 * This class is used to represent classes that store information about the current user, and
 * therefore, when the user's session is terminated, this information must be deleted.
 */
interface SessionStorage {
    suspend fun clearSessionData()
}
