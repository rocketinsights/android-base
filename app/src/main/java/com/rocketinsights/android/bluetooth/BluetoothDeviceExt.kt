package com.rocketinsights.android.bluetooth

import android.bluetooth.BluetoothDevice
import timber.log.Timber

private const val TAG = "BluetoothDevice"

private const val LOG_SEPARATOR = "==========================================="
private const val LOG_NEW_LINE = "\n"
private const val LOG_ITEM_SEPARATOR = "----------"

/**
 * There is not a direct way to remove bond, we must use reflection for it.
 */
fun BluetoothDevice.removeBond() {
    try {
        javaClass.getMethod("removeBond").invoke(this)
    } catch (e: Exception) {
        Timber.tag(TAG).e(e, "Removing bond has been failed. ${e.message}")
    }
}

fun BluetoothDevice.dump() {
    StringBuilder().apply {
        append(LOG_SEPARATOR)
        append("Dumping Bluetooth Device Data")
        append(LOG_NEW_LINE)
        append("Mac Address: $address")
        append(LOG_NEW_LINE)
        append("Name: $name")
        append(LOG_NEW_LINE)
        append(LOG_ITEM_SEPARATOR)
        append(LOG_NEW_LINE)
        append("List Of Services: ")
        append(LOG_NEW_LINE)
        append(LOG_NEW_LINE)

        uuids?.let {
            for (parcelUuid in it) {
                append("Service: " + parcelUuid.uuid.toString())
                append(LOG_NEW_LINE)
            }
        }

        append(LOG_NEW_LINE)
        append(LOG_SEPARATOR)
        append(LOG_NEW_LINE)

        Timber.tag(TAG).d(toString())
    }
}
