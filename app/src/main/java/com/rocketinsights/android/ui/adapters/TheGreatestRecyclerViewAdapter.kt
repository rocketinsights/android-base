package com.rocketinsights.android.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemTextBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.models.RecyclerViewItemModel

class TheGreatestRecyclerViewAdapter :
    ListAdapter<RecyclerViewItemModel, TheGreatestRecyclerViewAdapter.ViewHolder>(
        TheGreatestDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.viewBinding(ListItemTextBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecyclerViewItemModel) {
            binding.listItemTextView.text = item.text
        }
    }

    private class TheGreatestDiffCallback : DiffUtil.ItemCallback<RecyclerViewItemModel>() {

        override fun areItemsTheSame(oldItem: RecyclerViewItemModel, newItem: RecyclerViewItemModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RecyclerViewItemModel, newItem: RecyclerViewItemModel): Boolean =
            oldItem == newItem
    }
}
