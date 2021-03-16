package com.rocketinsights.android.managers.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.provider.Settings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.rocketinsights.android.managers.location.LocationManager.Companion.LOCATION_UPDATES_FASTEST_INTERVAL
import com.rocketinsights.android.managers.location.LocationManager.Companion.LOCATION_UPDATES_INTERVAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import java.util.ArrayList
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationManagerImpl(
    private val context: Context,
    /**
     * Disable if you don't want to allow the user to mock its location with apps like FakeGps
     */
    private val isMockLocationAllowed: Boolean = false
) : LocationManager {
    private var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private var locationSettings: SettingsClient = LocationServices.getSettingsClient(context)
    private var locationCallbacks: ArrayList<LocationCallback> = arrayListOf()

    private val locationRequest = LocationRequest().apply {
        interval = LOCATION_UPDATES_INTERVAL
        fastestInterval = LOCATION_UPDATES_FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun canGetLocation(): Boolean {
        val androidLocationManager = getAndroidLocationManager()
        val isGpsEnabled = isGpsEnabled()
        val isNetworkEnabled = androidLocationManager?.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
            ?: false
        return isGpsEnabled || isNetworkEnabled
    }

    override fun isGpsEnabled() = getAndroidLocationManager()?.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        ?: false

    private fun getAndroidLocationManager() =
        context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager?

    override fun askLocationAccessIntent() = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

    override fun distanceBetween(
        startLatLng: LatLng,
        endLatLng: LatLng,
        unit: LocationManager.DistanceUnit
    ): Double {
        val distanceInMeters = FloatArray(1)
        Location.distanceBetween(startLatLng.latitude,
            startLatLng.longitude,
            endLatLng.latitude,
            endLatLng.longitude,
            distanceInMeters)
        return distanceInMeters[0] * unit.multiplier
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): LatLng {
        checkLocationSettings(locationRequest)
        return suspendCoroutine { cont ->
            locationClient.lastLocation
                .addOnSuccessListener { lastLocation: Location? ->
                    if (lastLocation != null) {
                        cont.resume(LatLng(lastLocation.latitude, lastLocation.longitude))
                    } else {
                        cont.resumeWithException(LocationException.EmptyLocationException())
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resumeWithException(exception)
                }
        }
    }

    override suspend fun startLocationUpdates(): Flow<LocationUpdate> {
        checkLocationSettings(locationRequest)
        return requestLocationUpdates()
    }

    @SuppressLint("CheckResult")
    override suspend fun getFirstLocationUpdate() = startLocationUpdates()
        .onCompletion {
            stopLocationUpdates()
        }.first()

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(): Flow<LocationUpdate> {
        val updates = MutableSharedFlow<LocationUpdate>(
            replay = 0,
            extraBufferCapacity = 1
        )

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val lastLocation = locationResult?.lastLocation ?: return
                if (!isMockLocationAllowed && lastLocation.isFromMockProvider) {
                    Timber.w("Mock location not allowed")
                    updates.tryEmit(
                        LocationUpdate.Error(LocationException.MockLocationNotAllowed())
                    )
                    return
                }

                updates.tryEmit(
                    LocationUpdate.Success(LatLng(lastLocation.latitude, lastLocation.longitude))
                )
            }
        }

        locationCallbacks.add(locationCallback)
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        return updates.onCompletion {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    override suspend fun getAddressFromLatLng(latLng: LatLng): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val geocoderFromLocationResult = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (geocoderFromLocationResult.size > 0) {
            return geocoderFromLocationResult[0]
        } else {
            throw Throwable("No Address found")
        }
    }

    private suspend fun checkLocationSettings(locationRequest: LocationRequest): Unit =
        suspendCoroutine { cont ->
            val settingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)

            locationSettings.checkLocationSettings(settingsBuilder.build())
                .addOnSuccessListener {
                    cont.resume(Unit)
                }
                .addOnFailureListener {
                    Timber.w(it)
                    cont.resumeWithException(it)
                }
        }

    override fun stopLocationUpdates() {
        locationCallbacks.forEach { locationCallback -> locationClient.removeLocationUpdates(locationCallback) }
    }
}
