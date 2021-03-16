package com.rocketinsights.android.repos

import com.google.firebase.auth.FirebaseAuth
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.prefs.AuthLocalStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

private const val ERROR_REFRESHING_TOKEN = "Error occurred while refreshing token on the backend."
private const val ERROR_GET_TOKEN = "Error while retrieving user ID token."
private const val ERROR_GET_TOKEN_LOCAL =
    "Error while retrieving user ID token from the local store."

class AuthRepository(
    private val api: ApiService,
    private val prefs: AuthLocalStore,
    private val firebaseAuth: FirebaseAuth,
    dispatcher: DispatcherProvider
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.io() + job)

    private lateinit var authToken: StateFlow<String>

    init {
        scope.launch {
            authToken = prefs.getAuthToken().catch { e ->
                Timber.e(e, ERROR_GET_TOKEN_LOCAL)
                emit("")
            }.stateIn(scope)
        }
    }

    private val idTokenListener = FirebaseAuth.IdTokenListener { auth ->
        scope.launch {
            try {
                val tokenResult = auth.getIdToken()
                tokenResult?.token?.let { token ->
                    setAuthToken(token)
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
     * Post it to a dedicated backed API where user would be created/updated based on the token data.
     * Save auth token to the local store.
     * Saved token can be used to authenticate all other requests towards backend APIs.
     */
    private suspend fun setAuthToken(token: String) {
        if (token != authToken.value) {
            try {
                api.setAuthToken(token)
                prefs.setAuthToken(token)
            } catch (e: Throwable) {
                Timber.e(e, ERROR_REFRESHING_TOKEN)
            }
        }
    }
}