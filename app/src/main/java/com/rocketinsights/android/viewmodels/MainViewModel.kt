package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.ui.common.StringResProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val repo: MessageRepository,
    private val stringRes: StringResProvider
) : ViewModel() {

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> = _message

    init {
        _message.value = Message(stringRes.getString(R.string.loading))
        viewModelScope.launch {
            delay(2000) // just to simulate 2 sec delay - remove from production code
            try {
                val m = repo.getMessage()
                _message.value = m
            } catch (e: HttpException) {
                _message.value = Message(stringRes.getString(R.string.http_error))
            } catch (e: Throwable) {
                _message.value = Message(stringRes.getString(R.string.unknown_error))
            }
        }
    }
}