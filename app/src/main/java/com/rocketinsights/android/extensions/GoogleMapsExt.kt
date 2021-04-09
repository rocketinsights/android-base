package com.rocketinsights.android.extensions

import android.graphics.Bitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

fun GoogleMap.addMarker(position: LatLng, marker: Bitmap, autoRotate: Boolean = false): Marker {
    return addMarker(
        MarkerOptions()
            .flat(autoRotate)
            .position(position)
            .icon(BitmapDescriptorFactory.fromBitmap(marker))
    )
}

fun GoogleMap.changeCameraPosition(
    position: LatLng,
    cameraZoom: Float = 16f,
    bearing: Float = 0f,
    animate: Boolean = true
) {
    val cameraPosition = CameraPosition.Builder()
        .target(position)
        .zoom(cameraZoom)
        .bearing(bearing)
        .build()

    val cameraPositionUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

    if (animate) {
        animateCamera(cameraPositionUpdate)
    } else {
        moveCamera(cameraPositionUpdate)
    }
}

fun GoogleMap.changeCameraPosition(
    paddingFromEdges: Int,
    animate: Boolean = true,
    vararg positions: LatLng
) {
    val bounds = LatLngBounds.Builder()

    positions.forEach {
        bounds.include(it)
    }

    val cameraPositionUpdate = CameraUpdateFactory.newLatLngBounds(bounds.build(), paddingFromEdges)

    if (animate) {
        animateCamera(cameraPositionUpdate)
    } else {
        moveCamera(cameraPositionUpdate)
    }
}

fun GoogleMap.drawPolyline(
    decodedPointsList: List<LatLng>,
    polylineWidth: Float,
    color: Int
): Polyline {
    return addPolyline(
        PolylineOptions()
            .addAll(decodedPointsList)
            .width(polylineWidth)
            .color(color)
    )
}

fun List<List<Double>>.polylinePointsToLatLng(polylinePoints: List<List<Double>>): List<LatLng> {
    val points = ArrayList<LatLng>()
    var i = 0
    while (i < polylinePoints.size) {
        points.add(LatLng(polylinePoints[i][0], polylinePoints[i][1]))
        i++
    }
    return points
}
