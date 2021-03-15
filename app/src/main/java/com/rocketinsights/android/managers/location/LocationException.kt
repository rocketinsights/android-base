package com.rocketinsights.android.managers.location

sealed class LocationException : Exception() {
    class EmptyLocationException : LocationException()
    class MockLocationNotAllowed : LocationException()
}