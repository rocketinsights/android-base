package com.rocketinsights.android.viewmodels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.bluetooth.BleException
import com.rocketinsights.android.bluetooth.BluetoothManager
import com.rocketinsights.android.viewmodels.event.Event
import kotlinx.coroutines.launch

class BluetoothViewModel(
    private val bluetoothManager: BluetoothManager
) : ViewModel() {

    companion object {
        val BLUETOOTH_PERMISSIONS = BluetoothManager.BLUETOOTH_PERMISSIONS
    }

    private val _bluetoothActionState = MutableLiveData<Event<BluetoothActionState>>()
    val bluetoothActionState: LiveData<Event<BluetoothActionState>> = _bluetoothActionState

    val bluetoothState: LiveData<Int> = bluetoothManager.observeBluetoothStatus().asLiveData()

    val nearDevices: LiveData<BluetoothDevice> = bluetoothManager.observeBluetoothDiscovery().asLiveData()

    fun turnOnBluetooth() {
        viewModelScope.launch {
            try {
                _bluetoothActionState.value = Event(BluetoothActionState.TurnOn.Loading)
                bluetoothManager.turnOn()
                _bluetoothActionState.value = Event(BluetoothActionState.TurnOn.Done)
            } catch (e: Exception) {
                handlerError(e) {
                    _bluetoothActionState.value = Event(BluetoothActionState.TurnOn.Error(e))
                }
            }
        }
    }

    fun turnOffBluetooth() {
        viewModelScope.launch {
            try {
                _bluetoothActionState.value = Event(BluetoothActionState.TurnOff.Loading)
                bluetoothManager.turnOff()
                _bluetoothActionState.value = Event(BluetoothActionState.TurnOff.Done)
            } catch (e: Exception) {
                handlerError(e) {
                    _bluetoothActionState.value = Event(BluetoothActionState.TurnOff.Error(e))
                }
            }
        }
    }

    fun startNearBluetoothDevicesSearch() {
        viewModelScope.launch {
            try {
                _bluetoothActionState.value = Event(BluetoothActionState.Discovery.Loading)
                bluetoothManager.startDiscovery()
                _bluetoothActionState.value = Event(BluetoothActionState.Discovery.Done)
            } catch (e: Exception) {
                handlerError(e) {
                    _bluetoothActionState.value = Event(BluetoothActionState.Discovery.Error(e))
                }
            }
        }
    }

    fun stopNearBluetoothDevicesSearch() {
        viewModelScope.launch {
            try {
                _bluetoothActionState.value = Event(BluetoothActionState.Discovery.Loading)
                bluetoothManager.stopDiscovery()
                _bluetoothActionState.value = Event(BluetoothActionState.Discovery.Done)
            } catch (e: Exception) {
                handlerError(e) {
                    _bluetoothActionState.value = Event(BluetoothActionState.Discovery.Error(e))
                }
            }
        }
    }

    fun pairDevice(bluetoothDevice: BluetoothDevice) {
        viewModelScope.launch {
            try {
                _bluetoothActionState.value = Event(BluetoothActionState.Pair.Loading)
                bluetoothManager.pairDevice(bluetoothDevice)
                _bluetoothActionState.value = Event(BluetoothActionState.Pair.Done)
            } catch (e: Exception) {
                handlerError(e) {
                    _bluetoothActionState.value = Event(BluetoothActionState.Pair.Error(e))
                }
            }
        }
    }

    fun unpairDevice(bluetoothDevice: BluetoothDevice) {
        viewModelScope.launch {
            try {
                _bluetoothActionState.value = Event(BluetoothActionState.UnPair.Loading)
                bluetoothManager.pairDevice(bluetoothDevice)
                _bluetoothActionState.value = Event(BluetoothActionState.UnPair.Done)
            } catch (e: Exception) {
                handlerError(e) {
                    _bluetoothActionState.value = Event(BluetoothActionState.UnPair.Error(e))
                }
            }
        }
    }

    private fun handlerError(error: Exception, fallback: () -> Unit) {
        when (error) {
            is BleException.BluetoothNotSupportedException -> {
                _bluetoothActionState.value = Event(BluetoothActionState.Unsupported)
            }
            is BleException.BluetoothNotEnabledException -> {
                _bluetoothActionState.value = Event(BluetoothActionState.ServicesOff)
            }
            is BleException.BluetoothAccessNotGrantedException -> {
                _bluetoothActionState.value = Event(BluetoothActionState.PermissionsNeed)
            }
            else -> fallback.invoke()
        }
    }

    override fun onCleared() {
        bluetoothManager.stop()
        super.onCleared()
    }
}

sealed class BluetoothActionState {
    object Unsupported : BluetoothActionState()
    object ServicesOff : BluetoothActionState()
    object PermissionsNeed : BluetoothActionState()

    sealed class TurnOn : BluetoothActionState() {
        object Loading : TurnOn()
        object Done : TurnOn()
        class Error(error: Exception) : TurnOn()
    }

    sealed class TurnOff : BluetoothActionState() {
        object Loading : TurnOff()
        object Done : TurnOff()
        class Error(error: Exception) : TurnOff()
    }

    sealed class Pair : BluetoothActionState() {
        object Loading : Pair()
        object Done : Pair()
        class Error(error: Exception) : Pair()
    }

    sealed class UnPair : BluetoothActionState() {
        object Loading : UnPair()
        object Done : UnPair()
        class Error(error: Exception) : UnPair()
    }

    sealed class Discovery : BluetoothActionState() {
        object Loading : Discovery()
        object Done : Discovery()
        class Error(error: Exception) : Discovery()
    }
}
