package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentMessagesBinding
import com.rocketinsights.android.extensions.getIOErrorMessage
import com.rocketinsights.android.extensions.observeEvent
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.managers.InternetManager
import com.rocketinsights.android.ui.adapters.MessagesAdapter
import com.rocketinsights.android.viewmodels.ConnectivityViewModel
import com.rocketinsights.android.viewmodels.MessagesState
import com.rocketinsights.android.viewmodels.MessagesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Messages fragment shows list of messages from the remote API and cached in the local database.
 *
 * Contains examples of:
 *  - View binding,
 *  - Fade through transition,
 *  - RecyclerView with ListAdapter,
 *  - Setting up action bar,
 *  - Observing UI state,
 *  - Observing connectivity status.
 */
class MessagesFragment : Fragment(R.layout.fragment_messages) {
    private val binding by viewBinding(FragmentMessagesBinding::bind)
    private val viewModel by viewModel<MessagesViewModel>()
    private val connectivityViewModel by sharedViewModel<ConnectivityViewModel>()
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar(binding.toolbar)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.messagesRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration =
            DividerItemDecoration(binding.messagesRecyclerView.context, layoutManager.orientation)
        binding.messagesRecyclerView.addItemDecoration(dividerItemDecoration)
        messagesAdapter = MessagesAdapter()
        binding.messagesRecyclerView.adapter = messagesAdapter
    }

    private fun setupObservers() {
        // observe messages
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messagesAdapter.submitList(messages)
        }

        // observe UI state
        viewModel.messagesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MessagesState.Loading -> {
                    binding.loadingProgressBar.show()
                }
                is MessagesState.Success -> {
                    binding.loadingProgressBar.hide()
                }
                is MessagesState.Error -> {
                    binding.loadingProgressBar.hide()
                    val message = state.exception.getIOErrorMessage(requireContext())
                    requireContext().showToast(message)
                }
            }
        }

        // observe connectivity status
        connectivityViewModel.status.observeEvent(viewLifecycleOwner) { status ->
            if (status == InternetManager.ConnectivityStatus.CONNECTED) {
                viewModel.refreshMessages()
            } else {
                requireContext().showToast(getString(R.string.connectivity_offline))
            }
        }
    }

    private fun setScreenTransitions() {
        enterTransition = MaterialFadeThrough()
    }
}
