package com.rocketinsights.android.auth

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

private const val ERROR_GET_TOKEN = "Error while retrieving user ID token."

/**
 * This class observes the current authenticated user.
 * If there is no logged in user, AuthUser will be null.
 */
class AuthTokenLiveData : LiveData<String?>() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val idTokenListener = FirebaseAuth.IdTokenListener { auth ->
        auth.currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                value = task.result?.token
            } else {
                value = null
                Timber.e(task.exception, ERROR_GET_TOKEN)
            }
        }
    }

    override fun onActive() {
        firebaseAuth.addIdTokenListener(idTokenListener)
    }

    override fun onInactive() {
        firebaseAuth.removeIdTokenListener(idTokenListener)
    }
}