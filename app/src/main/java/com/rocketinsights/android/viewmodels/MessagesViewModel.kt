package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

private const val ERROR_GET_MESSAGES = "Error while retrieving and saving messages."

class MessagesViewModel(
    private val repo: MessageRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<MessagesFragmentState>(MessagesFragmentState.Loading)
    val viewState: LiveData<MessagesFragmentState> = _viewState

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    init {
        viewModelScope.launch {
            try {
                repo.getMessages().collect { messageList -> _messages.value = messageList }
                delay(2000) // just to simulate 2 sec delay - remove from production code
                repo.refreshMessages()
                _viewState.value = MessagesFragmentState.Success
            } catch (e: Throwable) {
                _viewState.value = MessagesFragmentState.Error(e)
                Timber.e(e, ERROR_GET_MESSAGES)
            }
        }
    }
}

sealed class MessagesFragmentState {
    object Loading : MessagesFragmentState()
    object Success : MessagesFragmentState()
    data class Error(val exception: Throwable) : MessagesFragmentState()
}