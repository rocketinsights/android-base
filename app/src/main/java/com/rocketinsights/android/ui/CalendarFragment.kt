package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentCalendarBinding
import com.rocketinsights.android.extensions.hide
import com.rocketinsights.android.extensions.observeEvent
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.show
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.adapters.EventsAdapter
import com.rocketinsights.android.viewmodels.CalendarState
import com.rocketinsights.android.viewmodels.CalendarViewModel
import com.rocketinsights.android.viewmodels.PermissionsResult
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Calendar fragment shows how to interact with the User's Default Calendar.
 *
 * Contains examples of:
 *  - Adding an Event in User's Default Calendar,
 *  - Visualize All Added Events
 */
class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private val binding by viewBinding(FragmentCalendarBinding::bind)
    private val viewModel by viewModel<CalendarViewModel>()
    private val permissionsViewModel: PermissionsViewModel by viewModel()

    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsViewModel.requestPermissions(
            this,
            *CalendarViewModel.CALENDAR_PERMISSIONS
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar(binding.toolbar)
        setupActions()
        setupEventsList()
        setupObservers()

        viewModel.refreshEvents()
    }

    private fun setupActions() {
        binding.addEvent.setOnClickListener {
            // TODO We can improve this an display a FragmentDialog with a form for the new Event
            viewModel.addEvent()
        }
    }

    private fun setupEventsList() {
        eventsAdapter = EventsAdapter()
        binding.events.addItemDecoration(listDivider)
        binding.events.adapter = eventsAdapter
    }

    private fun setupObservers() {
        // observe Events
        viewModel.events.observe(viewLifecycleOwner) { events ->
            eventsAdapter.submitList(events)
        }

        viewModel.calendarState.observeEvent(viewLifecycleOwner) { calendarState ->
            when (calendarState) {
                CalendarState.Loading -> binding.progress.show()
                CalendarState.Success -> {
                    binding.progress.hide()
                }
                is CalendarState.Error -> {
                    binding.progress.hide()
                    requireContext().showToast(
                        getString(R.string.calendar_error)
                    )
                }
            }
        }

        permissionsViewModel.permissionsResult.observeEvent(viewLifecycleOwner) { permissionResult ->
            if (permissionResult is PermissionsResult.PermissionsError) {
                requireContext().showToast(
                    getString(R.string.calendar_permission_not_granted)
                )
                findNavController().popBackStack()
            }
        }
    }

    private val listDivider: DividerItemDecoration by lazy {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        )

        val separatorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.list_item_separator_default)
        dividerItemDecoration.setDrawable(separatorDrawable!!)

        dividerItemDecoration
    }
}
