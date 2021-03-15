package com.rocketinsights.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.rocketinsights.android.auth.AuthUserLiveData
import com.rocketinsights.android.repos.AuthRepository

/**
 * Authentication VM exposes logged in status and user data to the view.
 */
class AuthViewModel(private val authRepository: AuthRepository, authUserLiveData: AuthUserLiveData) : ViewModel() {

    // change access modifier when you need to observe user data
    private val user = authUserLiveData

    val isLoggedIn = user.map { user -> user != null }

    init {
        authRepository.addIdTokenListener()
    }

    override fun onCleared() {
        authRepository.removeIdTokenListener()
        super.onCleared()
    }
}