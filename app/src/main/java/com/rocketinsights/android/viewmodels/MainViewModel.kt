package com.rocketinsights.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.os.Handler
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Message

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val message = MutableLiveData<Message>()

    init {
        message.postValue(Message(application.getString(R.string.loading)))
        Handler().postDelayed({
            message.postValue(Message(application.getString(R.string.done)))
        }, 5000)
    }
}