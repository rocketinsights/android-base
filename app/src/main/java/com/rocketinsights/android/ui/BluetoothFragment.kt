package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.rocketinsights.android.R
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.viewmodels.BluetoothViewModel
import com.rocketinsights.android.viewmodels.PermissionsResult
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BluetoothFragment : Fragment(R.layout.fragment_maps) {
    private val permissionsViewModel: PermissionsViewModel by viewModel()
    private val bluetoothViewModel: BluetoothViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsViewModel.requestPermissions(
            this,
            *BluetoothViewModel.BLUETOOTH_PERMISSIONS
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initUI() {
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
    }
}
