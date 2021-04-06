package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.auth.SessionStorage
import com.rocketinsights.android.auth.SessionWatcher
import com.rocketinsights.android.viewmodels.event.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

private const val ERROR_CLEARING_SESSION_DATA = "Error clearing session data."

class SessionViewModel(
    private val sessionWatcher: SessionWatcher,
    private vararg val sessionStorageContainers: SessionStorage
) : ViewModel() {

    private val _sessionDataSate = MutableLiveData<Event<SessionDataState>>()
    val sessionDataSate: LiveData<Event<SessionDataState>>
        get() = _sessionDataSate

    init {
        viewModelScope.launch {
            sessionWatcher
                .observeSessionCleared()
                .collect {
                    clearSessionData()
                }
        }
    }

    private fun clearSessionData() {
        viewModelScope.launch {
            _sessionDataSate.postValue(Event(SessionDataState.CLEARING))
            try {
                sessionStorageContainers.forEach {
                    it.clearSessionData()
                }
            } catch (error: Exception) {
                Timber.e(error, ERROR_CLEARING_SESSION_DATA)
            }
            _sessionDataSate.postValue(Event(SessionDataState.CLEARED))
        }
    }
}

sealed class SessionDataState {
    object CLEARING : SessionDataState()
    object CLEARED : SessionDataState()
}
