package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentMainBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private val mainViewModel: MainViewModel by viewModel()
    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupControls()
        setupObservers()
        updateUI()
    }

    private fun setupControls() {
        binding.stockImage.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.stockImage to "stockImage"
            )
            findNavController().navigate(MainFragmentDirections.actionGrowTransition(), extras)
        }
    }

    private fun setupObservers() {
        mainViewModel.message.observe(viewLifecycleOwner, { data ->
            binding.message.text = data?.text
            binding.stockImage.visibility = View.VISIBLE

            binding.message.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionSlideTransition())
            }
        })
    }

    private fun updateUI() {
        Glide.with(this).load(R.drawable.stock_image).centerCrop().into(binding.stockImage)
    }
}
