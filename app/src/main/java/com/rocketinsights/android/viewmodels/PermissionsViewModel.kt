package com.rocketinsights.android.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.managers.PermissionsManager
import com.rocketinsights.android.viewmodels.event.Event
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val manager: PermissionsManager
) : ViewModel() {

    private val _permissionsResult = MutableLiveData<Event<PermissionsResult>>()
    val permissionsResult: LiveData<Event<PermissionsResult>> = _permissionsResult

    fun requestPermission(fragment: Fragment, permissions: String) {
        viewModelScope.launch {
            try {
                manager.requestPermissions(fragment, permissions)
                _permissionsResult.value = Event(PermissionsResult.PermissionsGranted)
            } catch (e: Throwable) {
                _permissionsResult.value = Event(PermissionsResult.PermissionsError(e))
            }
        }
    }
}

sealed class PermissionsResult {
    object PermissionsGranted : PermissionsResult()
    data class PermissionsError(val exception: Throwable) : PermissionsResult()
}