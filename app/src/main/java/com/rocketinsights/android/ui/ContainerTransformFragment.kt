package com.rocketinsights.android.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentContainerTransformBinding
import com.rocketinsights.android.extensions.fadeIn
import com.rocketinsights.android.extensions.fadeOut
import com.rocketinsights.android.extensions.hide
import com.rocketinsights.android.extensions.invisible
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.show
import com.rocketinsights.android.extensions.viewBinding

/**
 * Container transform fragment shows an example of container transform transition between two `View`s.
 * There are two transitions:
 * - From button to card,
 * - From card to button.
 *
 * There is also a container transform transition from previous fragment to this one.
 */
class ContainerTransformFragment : Fragment(R.layout.fragment_container_transform) {

    private val binding by viewBinding(FragmentContainerTransformBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar(binding.toolbar)
        setupControls()
    }

    private fun setScreenTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = MaterialColors.getColor(requireContext(), R.attr.colorSecondary, "")
        }
        // remove return transition so that it doesn't interfere with reenter animation of previous fragment
        sharedElementReturnTransition = null
    }

    private fun setupControls() {
        binding.buttonTransformInfo.setOnClickListener {
            expandButton()
        }
    }

    private fun expandButton() {
        // setup and show auxiliary scrim
        binding.scrimInfoCard.run {
            show()
            setOnClickListener {
                collapseButton()
            }
        }
        // create and start expand transition from button to card
        val transform = MaterialContainerTransform().apply {
            startView = binding.buttonTransformInfo
            endView = binding.cardTransformInfo
            scrimColor = Color.TRANSPARENT
            addTarget(binding.cardTransformInfo)
        }
        TransitionManager.beginDelayedTransition(binding.root, transform)
        binding.run {
            textTransformButtonInfo.fadeOut()
            cardTransformInfo.show()
            buttonTransformInfo.invisible()
        }
    }

    private fun collapseButton() {
        // remove auxiliary scrim
        binding.scrimInfoCard.hide()
        // create and start collapse transition from card to button
        val transform = MaterialContainerTransform().apply {
            startView = binding.cardTransformInfo
            endView = binding.buttonTransformInfo
            scrimColor = Color.TRANSPARENT
            addTarget(binding.buttonTransformInfo)
        }
        TransitionManager.beginDelayedTransition(binding.root, transform)
        binding.run {
            buttonTransformInfo.show()
            cardTransformInfo.invisible()
            textTransformButtonInfo.fadeIn()
        }
    }
}
