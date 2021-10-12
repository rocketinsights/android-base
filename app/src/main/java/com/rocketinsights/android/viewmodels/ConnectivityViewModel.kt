package com.rocketinsights.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rocketinsights.android.managers.InternetManager
import com.rocketinsights.android.viewmodels.event.Event
import kotlinx.coroutines.flow.map

/**
 * Connectivity view model provides info about connectivity status via observable (starting with API 24).
 */
class ConnectivityViewModel(
    manager: InternetManager
) : ViewModel() {
    val status = manager.observeConnectivityStatus().map {
        Event(it)
    }.asLiveData()
}
