package com.rocketinsights.android.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.extensions.setEvent
import com.rocketinsights.android.managers.PermissionsManager
import com.rocketinsights.android.viewmodels.event.Event
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val manager: PermissionsManager
) : ViewModel() {

    private val _permissionsResult = MutableLiveData<Event<PermissionsResult>>()
    val permissionsResult: LiveData<Event<PermissionsResult>> get() = _permissionsResult

    fun requestPermissions(fragment: Fragment, vararg permissions: String) {
        viewModelScope.launch {
            try {
                manager.requestPermissions(fragment, *permissions)
                _permissionsResult.setEvent(PermissionsResult.PermissionsGranted)
            } catch (e: Throwable) {
                _permissionsResult.setEvent(PermissionsResult.PermissionsError(e))
            }
        }
    }
}

sealed class PermissionsResult {
    object PermissionsGranted : PermissionsResult()
    data class PermissionsError(val exception: Throwable) : PermissionsResult()
}
