package com.rocketinsights.android.managers.location

import android.Manifest
import android.content.Intent
import android.location.Address
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

sealed class LocationUpdate {
    data class Success(val latLng: LatLng) : LocationUpdate()
    data class Error(val exception: Exception) : LocationUpdate()
}

interface LocationManager {
    companion object {
        const val LOCATION_UPDATES_INTERVAL = 10000L // 10 seconds
        const val LOCATION_UPDATES_FASTEST_INTERVAL = 5000L // 5 seconds

        val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun canGetLocation(): Boolean

    fun isGpsEnabled(): Boolean

    fun askLocationAccessIntent(): Intent

    fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng, unit: DistanceUnit): Double

    /**
     * Location Access Required
     */
    suspend fun startLocationUpdates(): Flow<LocationUpdate>

    suspend fun getFirstLocationUpdate(): LocationUpdate

    suspend fun getLastLocation(): LatLng

    suspend fun getAddressFromLatLng(latLng: LatLng): Address?

    fun stopLocationUpdates()

    enum class DistanceUnit(val multiplier: Double) {
        METERS(1.0),
        MILES(0.000621371),
        FEET(3.28084)
    }
}
