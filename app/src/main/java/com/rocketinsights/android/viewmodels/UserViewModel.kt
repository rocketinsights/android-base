package com.rocketinsights.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.rocketinsights.android.repos.AuthRepository

/**
 * User VM exposes logged in status and user data to the view.
 */
class UserViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    // change access modifier when you need to observe user data
    private val user = authRepository.observeUser().asLiveData()

    val isLoggedIn = user.map { user -> user != null }
}
