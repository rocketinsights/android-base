package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentSecondBinding
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.common.BaseFragment

/**
 * Second fragment contains examples of:
 *  - View binding,
 *  - Save Args (Navigation Component),
 *  - Shared element transition,
 *  - Image loading with Coil.
 */
class SecondFragment : BaseFragment(R.layout.fragment_second) {
    private val binding by viewBinding(FragmentSecondBinding::bind)
    private val args: SecondFragmentArgs by navArgs()

    override fun doOnCreate(savedInstanceState: Bundle?) {
        if (args.hasSharedElement) {
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar(binding.toolbar)
        updateUI()
    }

    private fun updateUI() {
        binding.stockImage.load(R.drawable.stock_image)
    }
}
