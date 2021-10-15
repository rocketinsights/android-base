package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme
import com.rocketinsights.android.R
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.managers.InternetManager
import com.rocketinsights.android.ui.compose.PlayerScreen
import com.rocketinsights.android.viewmodels.ConnectivityViewModel
import com.rocketinsights.android.viewmodels.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private val connectivityViewModel by sharedViewModel<ConnectivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MdcTheme {
                PlayerScreen(playerViewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeConnectivityStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }

    private fun observeConnectivityStatus() {
        connectivityViewModel.status.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { status ->
                if (status == InternetManager.ConnectivityStatus.CONNECTED) {
                    if (playerViewModel.isError.value == true) {
                        playerViewModel.getPlayer()
                    }
                } else {
                    requireContext().showToast(getString(R.string.connectivity_offline))
                }
            }
        }
    }
}
