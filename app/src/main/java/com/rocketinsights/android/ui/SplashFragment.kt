package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentSplashBinding
import com.rocketinsights.android.extensions.viewBinding
import kotlinx.coroutines.delay

private const val FADE_OUT_DURATION = 1000L

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSystemUI()
        // for testing purposes
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(500)
            findNavController().navigate(SplashFragmentDirections.showMainFragment())
        }
    }

    override fun onDestroyView() {
        showSystemUI()
        super.onDestroyView()
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        ).show(WindowInsetsCompat.Type.navigationBars())
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        ).hide(WindowInsetsCompat.Type.navigationBars())
    }

    private fun setScreenTransitions() {
        exitTransition = MaterialFadeThrough().apply { duration = FADE_OUT_DURATION }
    }
}
