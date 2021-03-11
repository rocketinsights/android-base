package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rocketinsights.android.R
import com.rocketinsights.android.adapters.MessagesAdapter
import com.rocketinsights.android.databinding.FragmentMessagesBinding
import com.rocketinsights.android.extensions.getIOErrorMessage
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.MessagesFragmentState
import com.rocketinsights.android.viewmodels.MessagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessagesFragment : Fragment(R.layout.fragment_messages) {

    private val binding by viewBinding(FragmentMessagesBinding::bind)
    private val viewModel by viewModel<MessagesViewModel>()
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
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
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MessagesFragmentState.Loading -> {
                    binding.loadingProgressBar.show()
                }
                is MessagesFragmentState.Success -> {
                    binding.loadingProgressBar.hide()
                }
                is MessagesFragmentState.Error -> {
                    binding.loadingProgressBar.hide()
                    val message = state.exception.getIOErrorMessage(requireContext())
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}