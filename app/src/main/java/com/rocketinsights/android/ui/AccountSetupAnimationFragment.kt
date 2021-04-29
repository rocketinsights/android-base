package com.rocketinsights.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.rocketinsights.android.R

/**
 * A simple example of Lottie animation.
 * This screen shows loading animation which can be used on the account setup screen while
 * user's account is being set up on the backend.
 * It's also a destination of a container transformation (card view to fragment).
 */
class AccountSetupAnimationFragment : Fragment(R.layout.fragment_account_setup_animation) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenTransitions()
    }

    private fun setScreenTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = MaterialColors.getColor(requireContext(), R.attr.colorSecondary, "")
        }
        sharedElementReturnTransition = null
    }
}
