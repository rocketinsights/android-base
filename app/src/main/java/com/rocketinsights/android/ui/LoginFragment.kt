package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.AuthGraphDirections
import com.rocketinsights.android.R
import com.rocketinsights.android.auth.AuthManager
import com.rocketinsights.android.databinding.FragmentLoginBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.UserViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

@FlowPreview
/**
 * Login fragment is a starting point of auth flow.
 * It contains company logo, welcome message and login button.
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val userViewModel: UserViewModel by sharedViewModel()
    private val authManager: AuthManager by inject(parameters = { parametersOf(requireContext()) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenTransitions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
