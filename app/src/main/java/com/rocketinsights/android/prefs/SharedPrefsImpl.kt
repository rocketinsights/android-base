package com.rocketinsights.android.prefs

import android.content.Context

private const val AUTH_TOKEN_KEY = "AUTH_TOKEN"
private const val PREFERENCE_FILE_KEY = "com.rocketinsights.android.USER_PREFS"

class SharedPrefsImpl(context: Context) : SharedPrefs {

    private val sharedPref =
        context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)

    override fun getAuthToken(): String = sharedPref.getString(AUTH_TOKEN_KEY, "") ?: ""

    override fun setAuthToken(token: String) {
        sharedPref.edit()
            .putString(AUTH_TOKEN_KEY, token)
            .apply()
    }
}