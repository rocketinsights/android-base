package com.rocketinsights.android.extensions

import android.location.Address
import com.google.android.gms.maps.model.LatLng

fun Address.getAddress() = getAddressLine(0)

fun Address.getCity() = getAddressLine(1)

fun Address.getCountry() = getAddressLine(2)

fun Address.toLatLng() = LatLng(latitude, longitude)
