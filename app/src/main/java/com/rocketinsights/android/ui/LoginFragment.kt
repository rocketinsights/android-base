package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.AuthGraphDirections
import com.rocketinsights.android.R
import com.rocketinsights.android.auth.AuthManager
import com.rocketinsights.android.databinding.FragmentLoginBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.common.BaseFragment
import com.rocketinsights.android.viewmodels.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
 * Login fragment is a starting point of auth flow.
 * It contains company logo, welcome message and login button.
 */
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val userViewModel: UserViewModel by sharedViewModel()
    private val authManager: AuthManager by inject(parameters = { parametersOf(requireContext()) })

    override fun doOnCreate(savedInstanceState: Bundle?) {
        setScreenTransitions()
    }

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        setupControls()
        setupObservers()
    }

    private fun setScreenTransitions() {
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    private fun setupControls() {
        binding.buttonLogin.setOnClickListener {
            if (userViewModel.isLoggedIn.value == null || userViewModel.isLoggedIn.value == false) {
                authManager.launchSignInFlow()
            }
        }
    }

    private fun setupObservers() {
        userViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                findNavController().navigate(AuthGraphDirections.showMainFlow())
            }
        }
    }
}
