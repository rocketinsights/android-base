package com.rocketinsights.android.bluetooth

import androidx.annotation.IntDef
import com.rocketinsights.android.bluetooth.BluetoothState.Companion.BLUETOOTH_OFF
import com.rocketinsights.android.bluetooth.BluetoothState.Companion.BLUETOOTH_ON

@Retention(AnnotationRetention.SOURCE)
@IntDef(BLUETOOTH_ON, BLUETOOTH_OFF)
annotation class BluetoothState {
    companion object {
        const val BLUETOOTH_ON = 0
        const val BLUETOOTH_OFF = 1
    }
}
