package com.rocketinsights.android.bluetooth

import androidx.annotation.IntDef
import com.rocketinsights.android.bluetooth.BleOperationType.Companion.TYPE_READ
import com.rocketinsights.android.bluetooth.BleOperationType.Companion.TYPE_SUBSCRIBE
import com.rocketinsights.android.bluetooth.BleOperationType.Companion.TYPE_WRITE

@Retention(AnnotationRetention.SOURCE)
@IntDef(TYPE_SUBSCRIBE, TYPE_READ, TYPE_WRITE)
annotation class BleOperationType {
    companion object {
        const val TYPE_SUBSCRIBE = 0
        const val TYPE_READ = 1
        const val TYPE_WRITE = 2
    }
}
