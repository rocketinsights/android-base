package com.rocketinsights.android.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentPropertyAnimationBinding
import com.rocketinsights.android.extensions.disable
import com.rocketinsights.android.extensions.enable
import com.rocketinsights.android.extensions.fadeIn
import com.rocketinsights.android.extensions.fadeOut
import com.rocketinsights.android.extensions.viewBinding

private const val FADE_IN_DURATION = 500L
private const val FADE_OUT_DURATION = 500L
private const val ANIMATION_DURATION = 1000L
private const val SINGLE_ROTATION = 360F
private const val DOUBLE_ROTATION = 720F
private const val TRANSLATION = 150F

/**
 * Property animation fragment shows examples of `View` property animations.
 * There are few `Animator` classes to consider:
 * - `ViewPropertyAnimator` - Enables automatic and optimized animation of selected properties on `View` objects.
 *   Useful when animating several properties simultaneously, or if we want more convenient syntax to animate a specific property.
 *   It's limited to non-repeatable basic animations (e.g. fade-in or fade-out).
 * - `ObjectAnimator` - Can be used for all property animations. It animates single property by default,
 *   can repeat itself, and reverse itself.
 *   Use `PropertyValueHolders` to animate multiple properties on a single `View`.
 *   Use `AnimatorSet` for animating multiple views simultaneously, or you have one view but would
 *   like to sequence the animations one after the other. `AnimatorSet` doesn't allow repeating.
 * - `ValueAnimator` - Can be used for custom animations when animating something that isn't a property of a `View` (e.g. gradient).
 *
 * `MotionLayout` is recommended to use when animating UI elements with which users interact
 * (e.g. buttons, title bars, headers...). It can be used for complex animations on multiple views.
 * Motion in your app should help users to understand what your application is doing.
 *
 * `Lottie` is recommended to use for complex animations which are difficult to implement programmatically,
 * have to be scalable at runtime and look the same on multiple platforms (e.g. icon animations, rich graphical animations).
 */
class PropertyAnimationFragment : Fragment(R.layout.fragment_property_animation) {

    private val binding by viewBinding(FragmentPropertyAnimationBinding::bind)

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
        setupAnimation1()
        setupAnimation2()
        setupAnimation3()
        setupAnimation4()
    }

    /**
     * Call `ViewPropertyAnimator` for fade in/out animation, and disable the button while animating.
     */
    private fun setupAnimation1() {
        binding.buttonAnimation1.setOnClickListener {
            if (binding.imageBall1.isVisible) {
                binding.imageBall1.fadeOut(
                    duration = FADE_OUT_DURATION,
                    listener = disableButtonDuringAnimation(
                        button = binding.buttonAnimation1,
                        buttonText = getString(R.string.show)
                    )
                )
            } else {
                binding.imageBall1.fadeIn(
                    duration = FADE_IN_DURATION,
                    listener = disableButtonDuringAnimation(
                        button = binding.buttonAnimation1,
                        buttonText = getString(R.string.hide)
                    )
                )
            }
        }
    }

    /**
     * Call `ViewPropertyAnimator` for simultaneous rotate and translate animations,
     * than revert the animation.
     */
    private fun setupAnimation2() {
        binding.buttonAnimation2.setOnClickListener { button ->
            button.disable()
            binding.imageBall2.animate()
                .setDuration(ANIMATION_DURATION)
                .rotation(SINGLE_ROTATION)
                .translationXBy(TRANSLATION)
                .withEndAction {
                    binding.imageBall2.animate()
                        .setDuration(ANIMATION_DURATION)
                        .rotation(-SINGLE_ROTATION)
                        .translationXBy(-TRANSLATION)
                        .withEndAction { button.enable() }
                }
        }
    }

    /**
     * Use `PropertyValuesHolder` to setup rotate and translate animations.
     * Add animations to `ObjectAnimator` so that they run simultaneously, and indefinitely with reverse.
     */
    private fun setupAnimation3() {
        val rotation = PropertyValuesHolder.ofFloat(View.ROTATION, 0F, DOUBLE_ROTATION)
        val translation = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0F, TRANSLATION)
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(binding.imageBall3, rotation, translation)
                .apply {
                    duration = ANIMATION_DURATION
                    interpolator = AccelerateDecelerateInterpolator()
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.REVERSE
                }

        binding.buttonAnimation3.setOnClickListener {
            if (animator.isRunning) {
                animator.cancel()
                binding.buttonAnimation3.text = getString(R.string.start)
            } else {
                animator.start()
                binding.buttonAnimation3.text = getString(R.string.stop)
            }
        }
    }

    /**
     * Create rotation and color animations with `ObjectAnimator`s.
     * Add animations to `AnimatorSet` so run them sequentially.
     */
    private fun setupAnimation4() {
        val rotation =
            ObjectAnimator.ofFloat(binding.imageBall4, View.ROTATION, 0F, DOUBLE_ROTATION)
                .apply {
                    duration = ANIMATION_DURATION
                    interpolator = AccelerateDecelerateInterpolator()
                }

        val colorFrom = MaterialColors.getColor(binding.root, R.attr.colorPrimaryVariant)
        val colorTo = MaterialColors.getColor(binding.root, R.attr.colorSecondary)
        val color =
            ObjectAnimator.ofArgb(binding.imageBall4, "colorFilter", colorFrom, colorTo, colorFrom)
                .apply {
                    duration = ANIMATION_DURATION
                    interpolator = AccelerateDecelerateInterpolator()
                }

        val animation = AnimatorSet().apply {
            playSequentially(rotation, color)
            addListener(disableButtonDuringAnimation(binding.buttonAnimation4))
        }

        binding.buttonAnimation4.setOnClickListener {
            animation.start()
        }
    }

    private fun setScreenTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = MaterialColors.getColor(requireContext(), R.attr.colorSecondary, "")
        }
        sharedElementReturnTransition = null
    }
}

private fun disableButtonDuringAnimation(
    button: Button,
    buttonText: String? = null
): Animator.AnimatorListener =
    object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            button.disable()
        }

        override fun onAnimationEnd(animation: Animator?) {
            button.enable()
            buttonText?.let { button.text = buttonText }
        }
    }
