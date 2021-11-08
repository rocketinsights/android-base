package com.rocketinsights.android.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ui.IconGenerator
import com.rocketinsights.android.R
import com.rocketinsights.android.extensions.addMarker
import com.rocketinsights.android.extensions.changeCameraPosition
import com.rocketinsights.android.extensions.getAddress
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.ui.common.BaseFragment
import com.rocketinsights.android.viewmodels.LocationResult
import com.rocketinsights.android.viewmodels.LocationViewModel
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.scope.scopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : BaseFragment(R.layout.fragment_maps) {
    private val permissionsViewModel: PermissionsViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()
    private lateinit var parentScrollProvider: ParentScrollProvider

    private val requestGpsSwitchOn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // We don't need to do anything
        }

    override fun doOnCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        parentScrollProvider = requireNotNull(scopeActivity).get()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        setupObservers()
    }

    override fun doOnResume() {
        locationViewModel.retrieveCurrentLocation()
    }

    override fun doOnDestroyView() {
        parentScrollProvider.enableTouchOnParentScrollContainer(false)
    }

    private fun getMapsFragment() =
        childFragmentManager.findFragmentById(R.id.maps_fragment) as ScrollableMapFragment

    private fun initUI() {
        getMapsFragment()
            .setOnMapTouchListener {
                parentScrollProvider.enableTouchOnParentScrollContainer(true)
            }
    }

    private fun setupObservers() {
        locationViewModel.locationState.observe(
            viewLifecycleOwner,
            { result ->
                when (result) {
                    is LocationResult.Location -> {
                        (childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment)
                            .getMapAsync { map ->
                                val bubbleIconGenerator = IconGenerator(requireContext())
                                bubbleIconGenerator.apply {
                                    setColor(Color.BLUE)
                                    setTextAppearance(R.style.mapsBubbleMarker)
                                }

                                map.apply {
                                    addMarker(
                                        position = result.latLng,
                                        marker = bubbleIconGenerator.makeIcon(getString(R.string.maps_current_position)),
                                        autoRotate = true
                                    )
                                    changeCameraPosition(
                                        position = result.latLng
                                    )
                                }
                            }

                        result.address?.let {
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
            }
        )
    }
}
