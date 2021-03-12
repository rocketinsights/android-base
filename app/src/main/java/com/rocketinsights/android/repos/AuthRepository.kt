package com.rocketinsights.android.repos

import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.prefs.SharedPrefs
import kotlinx.coroutines.withContext
import timber.log.Timber

private const val ERROR_REFRESHING_TOKEN = "Error occurred while refreshing token on the backend."

class AuthRepository(private val api: ApiService, private val prefs: SharedPrefs, private val dispatcher: DispatcherProvider) {

    suspend fun refreshAuthToken(token: String) {
        withContext(dispatcher.io()) {
            if (token != prefs.getAuthToken()) {
                try {
                    api.refreshToken(token)
                    prefs.setAuthToken(token)
                }catch (e: Throwable) {
                    Timber.e(e, ERROR_REFRESHING_TOKEN)
                }
            }
        }
    }
}