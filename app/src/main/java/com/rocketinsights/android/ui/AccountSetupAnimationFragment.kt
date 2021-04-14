package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.rocketinsights.android.R

/**
 * A simple example of Lottie animation.
 * Fragment shows loading animation which can be used on the account setup screen while
 * user's account is being set up on the backend.
 */
class AccountSetupAnimationFragment : Fragment(R.layout.fragment_account_setup_animation) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }
}
