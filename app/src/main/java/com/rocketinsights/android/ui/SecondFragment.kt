package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentSecondBinding
import com.rocketinsights.android.extensions.viewBinding

/**
 * Second fragment contains examples of:
 *  - View binding,
 *  - Save Args (Navigation Component),
 *  - Shared element transition,
 *  - Image loading with Coil.
 */
class SecondFragment : Fragment(R.layout.fragment_second) {
    private val binding by viewBinding(FragmentSecondBinding::bind)
    private val args: SecondFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.hasSharedElement) {
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        updateUI()
    }

    private fun setupActionBar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }

    private fun updateUI() {
        binding.stockImage.load(R.drawable.stock_image)
    }
}
