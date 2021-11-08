package com.rocketinsights.android.ui

import android.os.Bundle
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.rocketinsights.android.R
import com.rocketinsights.android.ui.common.BaseFragment

/**
 * A simple example of Lottie animation.
 * This screen shows loading animation which can be used on the account setup screen while
 * user's account is being set up on the backend.
 * It's also a destination of a container transformation (card view to fragment).
 */
class AccountSetupAnimationFragment : BaseFragment(R.layout.fragment_account_setup_animation) {

    override fun doOnCreate(savedInstanceState: Bundle?) {
        setScreenTransitions()
    }

    private fun setScreenTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = MaterialColors.getColor(requireContext(), R.attr.colorSecondary, "")
        }
        sharedElementReturnTransition = null
    }
}
