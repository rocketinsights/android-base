package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.bumptech.glide.Glide
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentMainBinding
import com.rocketinsights.android.extensions.getIOErrorMessage
import com.rocketinsights.android.extensions.show
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.LocationResult
import com.rocketinsights.android.viewmodels.LocationViewModel
import com.rocketinsights.android.viewmodels.MainFragmentMessage
import com.rocketinsights.android.viewmodels.MainViewModel
import com.rocketinsights.android.viewmodels.PermissionsResult
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private val mainViewModel: MainViewModel by viewModel()
    private val permissionsViewModel: PermissionsViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()
    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupControls()
        setupObservers()
        updateUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.messages_fragment -> item.onNavDestinationSelected(findNavController())
            R.id.request_permissions -> {
                permissionsViewModel.requestPermissions(
                    this,
                    *LocationViewModel.LOCATION_PERMISSIONS
                )
                true
            }
            R.id.request_location -> {
                locationViewModel.retrieveCurrentLocation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupControls() {
        binding.stockImage.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.stockImage to "stockImage"
            )
            findNavController().navigate(MainFragmentDirections.actionGrowTransition(), extras)
        }
    }

    private fun setupObservers() {
        mainViewModel.message.observe(viewLifecycleOwner, { data ->
            when (data) {
                is MainFragmentMessage.Loading -> binding.message.text = getString(R.string.loading)
                is MainFragmentMessage.Success -> binding.message.text = data.message.text
                is MainFragmentMessage.Error -> binding.message.text = data.exception.getIOErrorMessage(requireContext())
            }

            binding.stockImage.show()

            binding.message.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionSlideTransition())
            }
        })

        permissionsViewModel.permissionsResult.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is PermissionsResult.PermissionsGranted -> {
                        requireContext().showToast(getString(R.string.permissions_granted))
                    }
                    is PermissionsResult.PermissionsError -> {
                        requireContext().showToast(getString(R.string.permissions_denied))
                    }
                }
            }
        })

        locationViewModel.locationState.observe(viewLifecycleOwner, {
            when (it) {
                is LocationResult.Location -> {
                    requireContext().showToast(getString(R.string.location_current, it.latLng))
                }
                is LocationResult.PermissionsNeeded -> {
                    permissionsViewModel.requestPermissions(
                        this,
                        *LocationViewModel.LOCATION_PERMISSIONS
                    )
                }
                is LocationResult.GpsOff -> {
                    // TODO Request GPS to turn on
                    // startIntentForResult(locationManager.askLocationAccessIntent(), REQUEST_LOCATION_ACCESS)
                }
                is LocationResult.Error -> {
                    requireContext().showToast("Error checking user location")
                }
            }
        })
    }

    private fun updateUI() {
        Glide.with(this).load(R.drawable.stock_image).centerCrop().into(binding.stockImage)
    }
}
