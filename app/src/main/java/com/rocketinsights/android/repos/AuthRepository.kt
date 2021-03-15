package com.rocketinsights.android.repos

import com.google.firebase.auth.FirebaseAuth
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.prefs.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

private const val ERROR_REFRESHING_TOKEN = "Error occurred while refreshing token on the backend."
private const val ERROR_GET_TOKEN = "Error while retrieving user ID token."

class AuthRepository(
    private val api: ApiService,
    private val prefs: SharedPrefs,
    private val firebaseAuth: FirebaseAuth,
    dispatcher: DispatcherProvider
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.io() + job)

    private val idTokenListener = FirebaseAuth.IdTokenListener { auth ->
        scope.launch {
            try {
                val tokenResult = auth.getIdToken()
                tokenResult?.token?.let { token ->
                    refreshAuthToken(token)
                }
            } catch (e: Throwable) {
                Timber.e(e, ERROR_GET_TOKEN)
            }
        }
    }

    fun addIdTokenListener() {
        firebaseAuth.addIdTokenListener(idTokenListener)
    }

    fun removeIdTokenListener() {
        firebaseAuth.removeIdTokenListener(idTokenListener)
    }

    private suspend fun FirebaseAuth.getIdToken() = coroutineScope {
        currentUser?.getIdToken(false)?.await()
    }

    /**
     * Firebase ID token can be reused on a backend.
     * Post it to a dedicated backed API so that user can be created/updated on the backend side.
     * Refresh auth token saved in the shared preferences.
     * Saved token can be used to authenticate all other requests on the backend.
     */
    private suspend fun refreshAuthToken(token: String) {
        if (token != prefs.getAuthToken()) {
            try {
                api.refreshToken(token)
                prefs.setAuthToken(token)
            } catch (e: Throwable) {
                Timber.e(e, ERROR_REFRESHING_TOKEN)
            }
        }
    }
}