package com.rocketinsights.android.adapters

import android.bluetooth.BluetoothDevice
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemMessageBinding
import com.rocketinsights.android.extensions.viewBinding

class BluetoothDevicesAdapter :
    ListAdapter<BluetoothDevice, BluetoothDevicesAdapter.ViewHolder>(BluetoothDeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.viewBinding(ListItemMessageBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BluetoothDevice) {
            /* binding.textMessage.text = item.text
             binding.textTime.text = DateUtils.formatDateTime(binding.root.context, item.timestamp, FORMAT_SHOW_TIME)*/
        }
    }

    private class BluetoothDeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {

        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
            oldItem.address == newItem.address

        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
            oldItem == newItem
    }
}
