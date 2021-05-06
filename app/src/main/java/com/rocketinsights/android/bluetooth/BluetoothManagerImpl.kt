package com.rocketinsights.android.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.rocketinsights.android.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BluetoothManagerImpl(
    private val context: Context,
    private val dispatcher: DispatcherProvider
) : BluetoothManager {
    companion object {
        const val TAG: String = "BluetoothManager"
    }

    init {
        registerToBluetoothChanges()
    }

    private val scope = CoroutineScope(dispatcher.io())

    private var isDiscoveringReceiverRegistered = false
    private var isPairingReceiverRegistered = false

    // Forever Observers
    private val bluetoothStatusObserver = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1
    )
    private val bluetoothStatusReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val action = intent?.action ?: return

            if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> bluetoothStatusObserver.tryEmit(BluetoothState.BLUETOOTH_OFF)
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                    }
                    BluetoothAdapter.STATE_ON -> bluetoothStatusObserver.tryEmit(BluetoothState.BLUETOOTH_ON)
                    BluetoothAdapter.STATE_TURNING_ON -> {
                    }
                }
            }
        }
    }
    private val discoveryObserver = MutableSharedFlow<BluetoothDevice>(
        replay = 0,
        extraBufferCapacity = 1
    )
    private val discoveryReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val action = intent?.action ?: return
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    discoveryObserver.tryEmit(it)
                }
            }
        }
    }

    sealed class PairingProcessResult {
        class Success(val device: BluetoothDevice) : PairingProcessResult()
        class Error(val error: Exception) : PairingProcessResult()
    }

    // One Time Observers
    private var pairingObserver = MutableStateFlow<PairingProcessResult?>(null)
    private val pairingReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val action = intent?.action ?: return

            // When discovery finds a device
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                if (state == BluetoothDevice.BOND_BONDING) {
                    // Bonding process is still working
                    // Essentially this means that the Confirmation Dialog is still visible
                    return // Do Nothing
                }
                if (state == BluetoothDevice.BOND_BONDED) {
                    // bonding process was successful
                    // also means that the user pressed OK on the Dialog

                    // Get the BluetoothDevice object from the Intent
                    intent.getParcelableExtra<BluetoothDevice?>(
                        BluetoothDevice.EXTRA_DEVICE
                    )?.let {
                        pairingObserver.tryEmit(PairingProcessResult.Success(it))
                    }

                    return
                }

                // Paired device not found means error/cancel during pairing process
                pairingObserver.tryEmit(PairingProcessResult.Error(BleException.PairingToBTDeviceException))
            }
        }
    }

    // ======== Start Bluetooth Status Related ========

    override suspend fun bluetoothStatus(): Int {
        return if (getBluetoothAdapter().isEnabled) {
            BluetoothState.BLUETOOTH_ON
        } else {
            BluetoothState.BLUETOOTH_OFF
        }
    }

    override fun observeBluetoothStatus(): Flow<Int> = bluetoothStatusObserver

    override suspend fun turnOn(): Unit = suspendCoroutine { cont ->
        val btAdapter = getBluetoothAdapter()
        if (!btAdapter.isEnabled) {
            scope.launch {
                // Wait for connection, turn on can take a while.
                bluetoothStatusObserver
                    .filter { status -> status == BluetoothState.BLUETOOTH_ON }
                    .collect {
                        cont.resume(Unit)
                    }
            }

            // Works only with ADMIN BT permission.
            btAdapter.enable()
        } else {
            // BT already enabled
            cont.resume(Unit)
        }
    }

    override suspend fun turnOff() {
        getBluetoothAdapter().apply {
            if (isEnabled) {
                disable()
            }
        }
    }

    // ======== End Bluetooth Status Related ========

    // ======== Start Pairing Related ========

    override fun pairedDevices() = getBluetoothPairedDevices()

    override suspend fun pairDevice(bluetoothDevice: BluetoothDevice?): Unit = suspendCoroutine { cont ->
        // Check if device is already paired
        if (getPairedDevice(bluetoothDevice!!.address) != null) {
            cont.resume(Unit)
            return@suspendCoroutine
        }

        scope.launch {
            pairingObserver = MutableStateFlow(null)
            pairingObserver
                .collect {
                    when (it) {
                        is PairingProcessResult.Success -> {
                            // We could double-check address here
                            // it.device == bluetoothDevice.address
                            cont.resume(Unit)
                        }
                        is PairingProcessResult.Error -> {
                            cont.resumeWithException(it.error)
                        }
                    }
                }
        }

        registerToBTPairingReceiver()
        bluetoothDevice.createBond()
    }

    override suspend fun startDiscovery() {
        getBluetoothAdapter().apply {
            if (!isDiscovering) {
                startDiscovery()
            }
        }
        registerToBTDiscoveryReceiver()
    }

    override suspend fun stopDiscovery() {
        unregisterToBTDiscoveryReceiver()
        getBluetoothAdapter().cancelDiscovery()
    }

    // ======== End Pairing Related ========

    // ======== Start Discovery Related ========
    override fun observeBluetoothDiscovery(): Flow<BluetoothDevice> = discoveryObserver

    override suspend fun unpair(deviceMacAddress: String?) {
        getPairedDevice(deviceMacAddress)?.removeBond()
    }

    override suspend fun discoverAndPairDevice(deviceMacAddress: String?) {
        stopDiscovery()
        startDiscovery()

        val device = observeBluetoothDiscovery()
            .filter { bluetoothDevice ->
                bluetoothDevice.address == deviceMacAddress
            }
            .firstOrNull() ?: return

        pairDevice(device)
        stopDiscovery()
    }

    override fun stop() {
        getBluetoothAdapter().cancelDiscovery()
        unregisterToBTDiscoveryReceiver()
        unregisterToBTPairingReceiver()
    }

    private fun registerToBluetoothChanges() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(bluetoothStatusReceiver, filter)
    }

    private fun registerToBTPairingReceiver() {
        if (!isPairingReceiverRegistered) {
            val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            context.registerReceiver(pairingReceiver, filter)
            isPairingReceiverRegistered = true
        }
    }

    // ======== End Discovery Related ========

    private fun unregisterToBTPairingReceiver() {
        if (isPairingReceiverRegistered) {
            context.unregisterReceiver(pairingReceiver)
            isPairingReceiverRegistered = false
        }
    }

    private fun registerToBTDiscoveryReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(discoveryReceiver, filter)
        isDiscoveringReceiverRegistered = true
    }

    private fun unregisterToBTDiscoveryReceiver() {
        if (isDiscoveringReceiverRegistered) {
            context.unregisterReceiver(discoveryReceiver)
            isDiscoveringReceiverRegistered = false
        }
    }

    private fun getBluetoothPairedDevices(): Set<BluetoothDevice> =
        getBluetoothAdapter().bondedDevices

    protected fun getBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
            ?: throw BleException.BluetoothNotSupportedException // Device does not support Bluetooth
    }

    private fun isDevicePaired(mac: String?) = getPairedDevice(mac) != null

    private fun getPairedDevice(mac: String?): BluetoothDevice? {
        return getBluetoothPairedDevices().firstOrNull { bluetoothDevice ->
            bluetoothDevice.address == mac
        }
    }
}
