package com.rocketinsights.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.auth.AuthTokenLiveData
import com.rocketinsights.android.auth.AuthUserLiveData
import com.rocketinsights.android.repos.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    // change access modifier when you need to observe user data
    private val user = AuthUserLiveData()

    val isLoggedIn = user.map { user -> user != null }

    val authToken = AuthTokenLiveData()

    fun refreshAuthToken(token: String) {
        viewModelScope.launch {
            repo.refreshAuthToken(token)
        }
    }
}