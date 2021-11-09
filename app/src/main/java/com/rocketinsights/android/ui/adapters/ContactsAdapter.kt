package com.rocketinsights.android.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemContactBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.models.Contact

class ContactsAdapter(private val onContactClicked: (Contact) -> Unit) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(parent.viewBinding(ListItemContactBinding::inflate), onContactClicked)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactViewHolder(
        private val binding: ListItemContactBinding,
        private val onItemClick: (Contact) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.listItemContactName.text = contact.name
            binding.root.setOnClickListener { onItemClick(contact) }
        }
    }

    private class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem == newItem
    }
}
