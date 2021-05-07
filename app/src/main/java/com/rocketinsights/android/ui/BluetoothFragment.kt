package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rocketinsights.android.R
import com.rocketinsights.android.bluetooth.BluetoothState.Companion.BLUETOOTH_ON
import com.rocketinsights.android.databinding.FragmentBluetoothBinding
import com.rocketinsights.android.extensions.disable
import com.rocketinsights.android.extensions.enable
import com.rocketinsights.android.extensions.remove
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.show
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.adapters.BluetoothDevicesAdapter
import com.rocketinsights.android.viewmodels.BluetoothActionState
import com.rocketinsights.android.viewmodels.BluetoothViewModel
import com.rocketinsights.android.viewmodels.PermissionsResult
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BluetoothFragment : Fragment(R.layout.fragment_bluetooth) {
    private val binding by viewBinding(FragmentBluetoothBinding::bind)
    private val permissionsViewModel: PermissionsViewModel by viewModel()
    private val bluetoothViewModel: BluetoothViewModel by viewModel()

    private lateinit var pairedDevicesAdapter: BluetoothDevicesAdapter
    private lateinit var nearDevicesAdapter: BluetoothDevicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsViewModel.requestPermissions(
            this,
            *BluetoothViewModel.BLUETOOTH_PERMISSIONS
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar(binding.toolbar)
        initUI()
        setupPairedDevicesList()
        setupNearDevicesList()
        setupObservers()
    }

    private fun initUI() {
        binding.bluetoothStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bluetoothViewModel.turnOnBluetooth()
            } else {
                bluetoothViewModel.turnOffBluetooth()
            }
        }

        binding.bluetoothDiscoverDevicesButton.setOnClickListener {
            bluetoothViewModel.startNearBluetoothDevicesSearch()
        }

        binding.bluetoothStopDiscoverDevicesButton.setOnClickListener {
            bluetoothViewModel.stopNearBluetoothDevicesSearch()
        }
    }

    private fun setupPairedDevicesList() {
        pairedDevicesAdapter = BluetoothDevicesAdapter { btDevice ->
            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(true)
                .setTitle(btDevice.name ?: "Unnamed Device")
                .setMessage("MAC Address: " + btDevice.address)
                .setPositiveButton(R.string.bluetooth_unpair) { _, _ ->
                    bluetoothViewModel.unpairDevice(btDevice)
                }
                .show()
        }

        binding.bluetoothPairedDevicesList.adapter = pairedDevicesAdapter
    }

    private fun setupNearDevicesList() {
        nearDevicesAdapter = BluetoothDevicesAdapter { btDevice ->
            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(true)
                .setTitle(btDevice.name ?: "Unnamed Device")
                .setMessage("MAC Address: " + btDevice.address)
                .setPositiveButton(R.string.bluetooth_pair) { _, _ ->
                    bluetoothViewModel.pairDevice(btDevice)
                }
                .show()
        }

        binding.bluetoothNearDevicesList.adapter = nearDevicesAdapter

    }

    private fun setupObservers() {
        permissionsViewModel.permissionsResult.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { permissionResult ->
                if (permissionResult is PermissionsResult.PermissionsError) {
                    requireContext().showToast(
                        getString(R.string.bluetooth_permission_not_granted)
                    )
                    findNavController().popBackStack()
                }
            }
        }

        bluetoothViewModel.bluetoothActionState.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled().let { btActionState ->
                when (btActionState) {
                    is BluetoothActionState.Unsupported -> {
                        requireContext().showToast(getString(R.string.bluetooth_not_supported))
                    }
                    is BluetoothActionState.ServicesOff -> {
                        requireContext().showToast(getString(R.string.bluetooth_off))
                    }
                    is BluetoothActionState.PermissionsNeed -> {
                        requireContext().showToast(getString(R.string.bluetooth_permissions_denied))
                    }
                    is BluetoothActionState.TurnOff.Loading,
                    is BluetoothActionState.TurnOn.Loading -> {
                        binding.bluetoothStatusSwitch.disable()
                    }
                    is BluetoothActionState.TurnOff.Error,
                    is BluetoothActionState.TurnOff.Done,
                    is BluetoothActionState.TurnOn.Error,
                    is BluetoothActionState.TurnOn.Done -> {
                        binding.bluetoothStatusSwitch.enable()
                    }
                    is BluetoothActionState.UnPair.Loading,
                    is BluetoothActionState.Pair.Loading -> {
                        binding.progress.show()
                    }
                    is BluetoothActionState.UnPair.Error,
                    is BluetoothActionState.Pair.Error -> {
                        binding.progress.remove()
                    }
                    is BluetoothActionState.Pair.Done -> {
                        requireContext().showToast(getString(R.string.bluetooth_pair_done))
                        binding.progress.remove()
                    }
                    is BluetoothActionState.UnPair.Done -> {
                        requireContext().showToast(getString(R.string.bluetooth_unpair_done))
                        binding.progress.remove()
                    }
                    is BluetoothActionState.DiscoveryStart.Loading -> {
                        binding.progress.show()
                    }
                    is BluetoothActionState.DiscoveryStart.Error -> {
                        binding.progress.remove()
                        binding.bluetoothDiscoverDevicesButton.show()
                    }
                    is BluetoothActionState.DiscoveryStart.Done -> {
                        binding.progress.remove()
                        binding.bluetoothDiscoverDevicesButton.remove()
                        binding.bluetoothStopDiscoverDevicesButton.show()
                    }
                    is BluetoothActionState.DiscoveryStop.Loading -> {
                        binding.progress.show()
                    }
                    is BluetoothActionState.DiscoveryStop.Error -> {
                        binding.progress.remove()
                        binding.bluetoothStopDiscoverDevicesButton.show()
                    }
                    is BluetoothActionState.DiscoveryStop.Done -> {
                        binding.progress.remove()
                        binding.bluetoothStopDiscoverDevicesButton.remove()
                        binding.bluetoothDiscoverDevicesButton.show()
                    }
                }
            }
        }

        bluetoothViewModel.bluetoothState.observe(viewLifecycleOwner) { btState ->
            binding.bluetoothStatusSwitch.isChecked = btState == BLUETOOTH_ON
        }

        bluetoothViewModel.pairedDevices.observe(viewLifecycleOwner) {
            pairedDevicesAdapter.submitList(it)
        }

        bluetoothViewModel.nearDevices.observe(viewLifecycleOwner) { btDevice ->
            val currentList = nearDevicesAdapter.currentList.toMutableList()
            // During discovery the same device could appear multiple times
            if (currentList.firstOrNull {
                    it.address == btDevice.address
                } == null) {
                currentList.add(btDevice)
                nearDevicesAdapter.submitList(currentList)
            }
        }
    }
}
