package com.rocketinsights.android.prefs

interface SharedPrefs {

    fun getAuthToken(): String

    fun setAuthToken(token: String)
}