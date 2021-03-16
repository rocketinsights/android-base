package com.rocketinsights.android.prefs

private const val AUTH_TOKEN_KEY = "AUTH_TOKEN"

class AuthLocalStoreImpl(private val localStore: LocalStore) : AuthLocalStore {

    override fun getAuthToken() = localStore.getStringValue(AUTH_TOKEN_KEY)

    override suspend fun setAuthToken(token: String) {
        localStore.setStringValue(AUTH_TOKEN_KEY, token)
    }
}