package com.rocketinsights.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val message = MutableLiveData<Message>()

    init {
        message.postValue(Message(application.getString(R.string.loading)))
        viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            message.postValue(Message(application.getString(R.string.done)))
        }
    }
}