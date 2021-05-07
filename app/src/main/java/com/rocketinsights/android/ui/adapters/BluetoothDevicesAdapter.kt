package com.rocketinsights.android.ui.adapters

import android.bluetooth.BluetoothDevice
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.databinding.ListItemBluetoothDeviceBinding
import com.rocketinsights.android.databinding.ListItemMessageBinding
import com.rocketinsights.android.extensions.viewBinding

class BluetoothDevicesAdapter(private val itemClickListener: (btDevice: BluetoothDevice) -> Unit) :
    ListAdapter<BluetoothDevice, BluetoothDevicesAdapter.ViewHolder>(BluetoothDeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.viewBinding(ListItemBluetoothDeviceBinding::inflate))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }

    class ViewHolder(private val binding: ListItemBluetoothDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BluetoothDevice, itemClickListener: (btDevice: BluetoothDevice) -> Unit) {
            binding.root.setOnClickListener {
                itemClickListener.invoke(item)
            }

            binding.bluetoothDeviceName.text = if (!item.name.isNullOrBlank()) {
                item.name
            } else {
                "Unnamed"
            }

            binding.bluetoothDeviceAddress.text = item.address
        }
    }

    private class BluetoothDeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
            oldItem.address == newItem.address

        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
            oldItem == newItem
    }
}
