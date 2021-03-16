package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.rocketinsights.android.managers.PermissionsManager
import com.rocketinsights.android.managers.location.LocationManager
import com.rocketinsights.android.managers.location.LocationUpdate
import kotlinx.coroutines.launch

class LocationViewModel(
    private val permissionsManager: PermissionsManager,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _locationState = MutableLiveData<LocationResult>()
    val locationState: LiveData<LocationResult> = _locationState

    companion object {
        val LOCATION_PERMISSIONS = LocationManager.LOCATION_PERMISSIONS
    }

    fun retrieveCurrentLocation() {
        viewModelScope.launch {
            try {
                if (!permissionsManager.hasPermissions(*LOCATION_PERMISSIONS)) {
                    _locationState.value = LocationResult.PermissionsNeeded
                    return@launch
                }

                if (!locationManager.canGetLocation()) {
                    _locationState.value = LocationResult.GpsOff
                    return@launch
                }

                locationManager.getFirstLocationUpdate().let {
                    when (it) {
                        is LocationUpdate.Success -> {
                            _locationState.value = LocationResult.Location(it.latLng)
                        }
                        is LocationUpdate.Error -> {
                            _locationState.value = LocationResult.Error(it.exception)
                        }
                    }
                }
            } catch (e: Throwable) {
                _locationState.value = LocationResult.Error(e)
            }
        }
    }
}

sealed class LocationResult {
    data class Location(val latLng: LatLng) : LocationResult()
    object GpsOff : LocationResult()
    object PermissionsNeeded : LocationResult()
    data class Error(val exception: Throwable) : LocationResult()
}