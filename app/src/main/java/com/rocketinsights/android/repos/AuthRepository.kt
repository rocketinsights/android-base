package com.rocketinsights.android.repos

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rocketinsights.android.auth.AuthUser
import com.rocketinsights.android.auth.SessionWatcher
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.prefs.AuthLocalStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
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
) : SessionWatcher {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.io() + job)

    private lateinit var authToken: StateFlow<String>
    private var idTokenListener: FirebaseAuth.IdTokenListener? = null

    private val userObserver = MutableStateFlow<AuthUser?>(null)

    private val sessionClearedObserver = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1
    )

    init {
        scope.launch {
            authToken = prefs.getAuthToken().catch { e ->
                Timber.e(e, ERROR_GET_TOKEN_LOCAL)
                emit("")
            }.stateIn(scope)
        }

        firebaseAuth.addAuthStateListener {
            userObserver.tryEmit(it.currentUser?.toAuthUser())

            if (it.currentUser == null) {
                sessionClearedObserver.tryEmit(true)
                removeIdTokenListener()
            } else {
                addIdTokenListener()
            }
        }
    }

    private fun addIdTokenListener() {
        removeIdTokenListener()
        idTokenListener = FirebaseAuth.IdTokenListener { auth ->
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
        }.apply {
            firebaseAuth.addIdTokenListener(this)
        }
    }

    private fun removeIdTokenListener() {
        idTokenListener?.let {
            firebaseAuth.removeIdTokenListener(it)
        }
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

    fun observeUser(): Flow<AuthUser?> = userObserver

    override suspend fun isSignedIn(): Boolean = firebaseAuth.currentUser != null

    override suspend fun refreshAccessToken(): String? = getAccessToken()

    override suspend fun getAccessToken(): String? = prefs.getAuthToken().firstOrNull()

    override fun observeSessionCleared(): Flow<Boolean> = sessionClearedObserver

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun registerNotificationsToken(token: String) {
        api.registerNotificationsToken(token)
    }
}

private fun FirebaseUser.toAuthUser() = AuthUser(
    uid = uid,
    providerId = providerId,
    displayName = displayName ?: "",
    photoUrl = photoUrl,
    email = email ?: "",
    phoneNumber = phoneNumber ?: ""
)