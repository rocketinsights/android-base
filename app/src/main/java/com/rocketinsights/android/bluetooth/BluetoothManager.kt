package com.rocketinsights.android.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

/**
 * BLE Generic Operations:
 * - Turn On / Off
 * - Discover Devices
 * - Pairing / Unpairing
 *
 * Once you got your device paired you must use a BluetoothDeviceManager Instance to perform I/O
 * events.
 */
interface BluetoothManager {
    companion object {
        val BLUETOOTH_PERMISSIONS = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // Status
    suspend fun bluetoothStatus(): Int
    fun observeBluetoothStatus(): Flow<Int>

    // Turn On / Off
    suspend fun turnOn()
    suspend fun turnOff()

    // Devices
    fun pairedDevices(): Set<BluetoothDevice>

    // Discovery
    suspend fun startDiscovery()
    suspend fun stopDiscovery()
    fun observeBluetoothDiscovery(): Flow<BluetoothDevice>

    // Actions
    suspend fun pairDevice(bluetoothDevice: BluetoothDevice?)
    suspend fun unpair(deviceMacAddress: String?)
    suspend fun discoverAndPairDevice(deviceMacAddress: String?)

    // Stop / Clear states
    fun stop()
}
