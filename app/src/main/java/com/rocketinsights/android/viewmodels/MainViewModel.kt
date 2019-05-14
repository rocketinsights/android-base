package com.rocketinsights.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    application: Application,
    private val repo: MessageRepository
) : AndroidViewModel(application) {
    val message = MutableLiveData<Message>()

    init {
        message.postValue(Message(application.getString(R.string.loading)))
        viewModelScope.launch {
            delay(2000)
            try {
                val m = repo.getMessageAsync().await()
                message.postValue(m)
            } catch (e: HttpException) {
                message.postValue(Message(application.getString(R.string.http_error)))
            } catch (e: Throwable) {
                message.postValue(Message(application.getString(R.string.unknown_error)))
            }
        }
    }
}