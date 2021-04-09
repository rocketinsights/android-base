package com.rocketinsights.android.network

import com.rocketinsights.android.auth.SessionWatcher
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class TokenAuthenticator(
    private val sessionWatcher: SessionWatcher?
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            if (response.code == HTTP_UNAUTHORIZED) {
                // SessionWatcher is needed for handling Unauthorized errors.
                sessionWatcher ?: return null

                // Only retry 1 time
                if (retryCount(response) == 0 && runBlocking { sessionWatcher.isSignedIn() }) {
                    val requestBuilder = response.request.newBuilder()

                    // Retrieve refreshed token
                    runBlocking {
                        requestBuilder.header(
                            NetworkingManager.HEADER_AUTH,
                            NetworkingManager.HEADER_BEARER +
                                sessionWatcher.refreshAccessToken()
                        )
                    }

                    Timber.w("Token Expired/Invalid - Retrying request with a new one")
                    return requestBuilder.build()
                }

                // Logout the user... Needs to re-enter their credentials
                runBlocking {
                    sessionWatcher.signOut()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "TokenAuthenticator")
        }

        return null
    }

    private fun retryCount(response: Response?): Int {
        var priorResponse = response
        var result = 0
        while (priorResponse?.priorResponse != null) {
            priorResponse = priorResponse.priorResponse
            result++
        }
        return result
    }
}
