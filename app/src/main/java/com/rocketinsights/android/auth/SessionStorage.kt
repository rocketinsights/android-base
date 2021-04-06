package com.rocketinsights.android.auth

/**
 * This interface should be implemented by the classes that store information about the current
 * user, and therefore, when the user's session is terminated, this information must be deleted.
 */
interface SessionStorage {
    suspend fun clearSessionData()
}
