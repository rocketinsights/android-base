package com.rocketinsights.android.auth

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * This class observes the current authenticated user.
 * If there is no logged in user, AuthUser will be null.
 */
class AuthUserLiveData(private val firebaseAuth: FirebaseAuth) : LiveData<AuthUser?>() {

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser?.toAuthUser()
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
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