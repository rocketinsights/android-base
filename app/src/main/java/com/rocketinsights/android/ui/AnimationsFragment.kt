package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentAnimationsBinding
import com.rocketinsights.android.extensions.viewBinding

/**
 * Animations fragment contains cards which can be used to launch different animations.
 * This screen also runs ConstraintSet animation on cards and subtitle by using MotionLayout.
 */
class AnimationsFragment : Fragment(R.layout.fragment_animations) {

    private val binding by viewBinding(FragmentAnimationsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setScreenTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupControls()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }

    private fun setupControls() {
        binding.cardLottieAnimation.setOnClickListener {
            findNavController().navigate(AnimationsFragmentDirections.showAccountSetupAnimationFragment())
        }

        binding.cardPropertyAnimation.setOnClickListener {
            findNavController().navigate(AnimationsFragmentDirections.showPropertyAnimationFragment())
        }
    }

    private fun setScreenTransitions() {
        enterTransition = MaterialFadeThrough()
    }
}
