package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
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
        binding.cardLottieAnimation.setOnClickListener { card ->
            val extras = FragmentNavigatorExtras(card to getString(R.string.card_lottie_transition))
            findNavController().navigate(
                AnimationsFragmentDirections.showAccountSetupAnimationFragment(),
                extras
            )
        }

        binding.cardPropertyAnimation.setOnClickListener { card ->
            val extras =
                FragmentNavigatorExtras(card to getString(R.string.card_property_animation_transition))
            findNavController().navigate(
                AnimationsFragmentDirections.showPropertyAnimationFragment(),
                extras
            )
        }

        binding.cardContainerTransform.setOnClickListener { card ->
            val extras =
                FragmentNavigatorExtras(card to getString(R.string.card_container_transition))
            findNavController().navigate(
                AnimationsFragmentDirections.showContainerTransformFragment(),
                extras
            )
        }
    }

    private fun setScreenTransitions() {
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialElevationScale(true)
        exitTransition = MaterialElevationScale(false)
    }
}
