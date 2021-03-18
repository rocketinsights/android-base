package com.rocketinsights.android.managers

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

class InternetManager(
    private val connectivityManager: ConnectivityManager
) {
    enum class ConnectivityStatus {
        CONNECTED,
        NOT_CONNECTED
    }

    val connectivityStatus = MutableSharedFlow<ConnectivityStatus>(
        replay = 0,
        extraBufferCapacity = 1
    )

    init {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        connectivityStatus.tryEmit(ConnectivityStatus.CONNECTED)
                    }

                    override fun onLost(network: Network) {
                        connectivityStatus.tryEmit(ConnectivityStatus.NOT_CONNECTED)
                    }
                }
            )
        }
    }

    val isOnline: Boolean
        get() {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }

    fun observeConnectivityStatus(): Flow<ConnectivityStatus> =
        connectivityStatus
            .debounce(500)
}
