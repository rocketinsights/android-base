package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentMainBinding
import com.rocketinsights.android.extensions.show
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.MainFragmentMessage
import com.rocketinsights.android.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException

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
            findNavController().navigate(R.id.actionGrowTransition, null, null, extras)
        }
    }

    private fun setupObservers() {
        mainViewModel.message.observe(viewLifecycleOwner, { data ->
            when (data) {
                is MainFragmentMessage.Loading -> binding.message.text = getString(R.string.loading)
                is MainFragmentMessage.Success -> binding.message.text = data.message.text
                is MainFragmentMessage.Error -> binding.message.text =
                    if (data.exception is HttpException) getString(R.string.http_error)
                    else getString(R.string.unknown_error)
            }

            binding.stockImage.show()

            binding.message.setOnClickListener {
                findNavController().navigate(R.id.actionSlideTransition)
            }
        })
    }

    private fun updateUI() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "First Fragment"
        Glide.with(this).load(R.drawable.stock_image).centerCrop().into(binding.stockImage)
    }
}
