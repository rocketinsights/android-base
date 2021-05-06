package com.rocketinsights.android.bluetooth

sealed class BleException(message: String?) : Exception(message) {
    object BluetoothDeviceUnpairedException : BleException("The requested device got unpaired.")
    object BluetoothNotEnabledException : BleException("Bluetooth is Off.")
    object BluetoothAccessNotGrantedException : BleException("Bluetooth access not granted.")
    object BluetoothNotSupportedException : BleException("The current device doesn't support Bluetooth.")
    object PairingToBTDeviceException : BleException(
        "Couldn't pair to the selected Bluetooth device. Check if it is in pairing " +
            "mode and in the appropriate range."
    )
}
