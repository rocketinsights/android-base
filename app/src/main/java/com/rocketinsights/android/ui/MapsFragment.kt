package com.rocketinsights.android.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ui.IconGenerator
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentSecondBinding
import com.rocketinsights.android.extensions.addMarker
import com.rocketinsights.android.extensions.changeCameraPosition
import com.rocketinsights.android.extensions.getAddress
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.LocationResult
import com.rocketinsights.android.viewmodels.LocationViewModel
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment(R.layout.fragment_maps) {
    private val binding by viewBinding(FragmentSecondBinding::bind)
    private val permissionsViewModel: PermissionsViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()

    private val requestGpsSwitchOn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // We don't need to do anything
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.retrieveCurrentLocation()
    }

    override fun onDestroyView() {
        (requireActivity() as MainActivity).getScrollView().apply {
            requestDisallowInterceptTouchEvent(false)
        }
        super.onDestroyView()
    }

    private fun getMapsFragment() = childFragmentManager.findFragmentById(R.id.mapsFragment) as ScrollableMapFragment

    private fun initUI() {
        getMapsFragment()
            .setOnMapTouchListener {
                (requireActivity() as MainActivity).getScrollView().apply {
                    requestDisallowInterceptTouchEvent(true)
                }
            }
    }

    private fun setupObservers() {
        locationViewModel.locationState.observe(viewLifecycleOwner, {
            when (it) {
                is LocationResult.Location -> {
                    (childFragmentManager.findFragmentById(R.id.mapsFragment) as SupportMapFragment)
                        .getMapAsync { map ->
                            val bubbleIconGenerator = IconGenerator(requireContext())
                            bubbleIconGenerator.apply {
                                setColor(Color.BLUE)
                                setTextAppearance(R.style.mapsBubbleMarker)
                            }

                            map.apply {
                                addMarker(
                                    position = it.latLng,
                                    marker = bubbleIconGenerator.makeIcon(getString(R.string.maps_current_position)),
                                    autoRotate = true
                                )
                                changeCameraPosition(
                                    position = it.latLng
                                )
                            }
                        }

                    it.address?.let {
                        requireContext().showToast(
                            getString(R.string.location_current_address, it.getAddress())
                        )
                    }
                }
                is LocationResult.PermissionsNeeded -> {
                    permissionsViewModel.requestPermissions(
                        this,
                        *LocationViewModel.LOCATION_PERMISSIONS
                    )
                }
                is LocationResult.GpsOff -> {
                    requestGpsSwitchOn.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                is LocationResult.Error -> {
                    requireContext().showToast(getString(R.string.location_error))
                }
            }
        })
    }
}
