package com.rocketinsights.android.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemTextBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.TheGreatestRecyclerViewViewModel

class TheGreatestRecyclerViewAdapter :
    ListAdapter<TheGreatestRecyclerViewViewModel.ItemModel, TheGreatestRecyclerViewAdapter.ViewHolder>(
        TheGreatestDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.viewBinding(ListItemTextBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TheGreatestRecyclerViewViewModel.ItemModel) {
            binding.listItemTextView.text = item.text
        }
    }

    private class TheGreatestDiffCallback :
        DiffUtil.ItemCallback<TheGreatestRecyclerViewViewModel.ItemModel>() {

        override fun areItemsTheSame(
            oldItem: TheGreatestRecyclerViewViewModel.ItemModel,
            newItem: TheGreatestRecyclerViewViewModel.ItemModel
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TheGreatestRecyclerViewViewModel.ItemModel,
            newItem: TheGreatestRecyclerViewViewModel.ItemModel
        ): Boolean =
            oldItem == newItem
    }
}
