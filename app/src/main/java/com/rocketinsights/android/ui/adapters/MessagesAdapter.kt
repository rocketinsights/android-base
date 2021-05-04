package com.rocketinsights.android.ui.adapters

import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_SHOW_TIME
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemMessageBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.models.Message

class MessagesAdapter : ListAdapter<Message, MessagesAdapter.ViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.viewBinding(ListItemMessageBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message) {
            binding.textMessage.text = item.text
            binding.textTime.text = DateUtils.formatDateTime(binding.root.context, item.timestamp, FORMAT_SHOW_TIME)
        }
    }

    private class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem == newItem
    }
}
