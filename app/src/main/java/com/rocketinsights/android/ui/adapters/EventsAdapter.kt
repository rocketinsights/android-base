package com.rocketinsights.android.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemEventBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.managers.CalendarEvent

class EventsAdapter : ListAdapter<CalendarEvent, EventsAdapter.ViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.viewBinding(ListItemEventBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarEvent) {
            binding.eventTitle.text = item.title
            binding.eventDescription.text = item.description
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<CalendarEvent>() {
        override fun areItemsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent): Boolean =
            oldItem == newItem
    }
}
