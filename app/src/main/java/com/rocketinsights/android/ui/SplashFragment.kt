package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentSplashBinding
import com.rocketinsights.android.extensions.hideNavBar
import com.rocketinsights.android.extensions.showNavBar
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.common.BaseFragment
import com.rocketinsights.android.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val FADE_OUT_DURATION = 1000L
private const val SHOW_SCREEN_DURATION = 500L

/**
 * Splash fragment represents app's splash screen with logo and background.
 * After short delay it navigates to main flow, if the user is logged in, or to auth flow otherwise.
 */
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val userViewModel: UserViewModel by sharedViewModel()

    override fun doOnCreate(savedInstanceState: Bundle?) {
        setScreenTransitions()
    }

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        hideNavBar(binding.root)
        setupObservers()
    }

    override fun doOnDestroyView() {
        showNavBar(binding.root)
    }

    private fun setupObservers() {
        userViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            when (isLoggedIn) {
                true -> runAfterDelay {
                    findNavController().navigate(SplashFragmentDirections.showMainFlow())
                }
                false -> runAfterDelay {
                    findNavController().navigate(SplashFragmentDirections.showAuthFlow())
                }
            }
        }
    }

    private fun runAfterDelay(delay: Long = SHOW_SCREEN_DURATION, action: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(delay)
            action()
        }
    }

    private fun setScreenTransitions() {
        exitTransition = MaterialFadeThrough().apply { duration = FADE_OUT_DURATION }
    }
}
