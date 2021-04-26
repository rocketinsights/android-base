package com.rocketinsights.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.rocketinsights.android.repos.AuthRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

private const val DEBOUNCE_TIMEOUT = 500L

/**
 * User VM exposes logged in status and user data to the view.
 */
@FlowPreview
class UserViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    // change access modifier when you need to observe user data
    private val user = authRepository.observeUser().debounce(DEBOUNCE_TIMEOUT).asLiveData()

    val isLoggedIn = user.map { user -> user != null }
}
