package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val ERROR_GET_MESSAGE = "Error getting messages from the remote API."

class MainViewModel(
    private val repo: MessageRepository
) : ViewModel() {

    private val _messageState = MutableLiveData<MainMessageState>(MainMessageState.Loading)
    val messageState: LiveData<MainMessageState> = _messageState

    init {
        viewModelScope.launch {
            delay(2000) // just to simulate 2 sec delay - remove from production code
            try {
                val m = repo.getMessage()
                _messageState.value = MainMessageState.Success(m)
            } catch (e: Throwable) {
                _messageState.value = MainMessageState.Error(e)
                Timber.e(e, ERROR_GET_MESSAGE)
            }
        }
    }
}

sealed class MainMessageState {
    object Loading : MainMessageState()
    data class Success(val message: Message) : MainMessageState()
    data class Error(val exception: Throwable) : MainMessageState()
}