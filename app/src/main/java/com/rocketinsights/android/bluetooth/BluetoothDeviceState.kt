package com.rocketinsights.android.bluetooth

import androidx.annotation.IntDef
import com.rocketinsights.android.bluetooth.BluetoothDeviceState.Companion.CONNECTED
import com.rocketinsights.android.bluetooth.BluetoothDeviceState.Companion.DISCONNECTED

@Retention(AnnotationRetention.SOURCE)
@IntDef(CONNECTED, DISCONNECTED)
annotation class BluetoothDeviceState {
    companion object {
        const val CONNECTED = 0
        const val DISCONNECTED = 1
    }
}
