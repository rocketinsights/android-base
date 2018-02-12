package com.rocketinsights.android.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Message

/**
 * Created by brian on 8/21/17.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    val message = MutableLiveData<Message>()

    init {
        message.value = Message(application.getString(R.string.loading))
        Handler().postDelayed({
            message.value = Message(application.getString(R.string.done))
        }, 5000)
    }
}