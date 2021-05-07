package com.rocketinsights.android.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

/**
 * Bluetooth Manager allows us to perform the basic bluetooth actions: turn on, turn off,
 * search for devices, pair and un-pair.
 *
 * The sending of operations (read, write and subscribe) to a BLE device is pending.
 */
interface BluetoothManager {
    companion object {
        val BLUETOOTH_PERMISSIONS = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
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
