package com.rocketinsights.android.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.rocketinsights.android.NavGraphDirections
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.ActivityMainBinding
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.UserViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
class MainActivity : ScopeActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var navController: NavController
    private lateinit var destinationChangedListener: NavController.OnDestinationChangedListener
    private val userViewModel: UserViewModel by viewModel()
    private var notLoginScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(binding.root)
        setNavController()
        observeUserLoginStatus()
    }

    override fun onDestroy() {
        navController.removeOnDestinationChangedListener(destinationChangedListener)
        super.onDestroy()
    }

    private fun setNavController() {
        navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
        destinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                notLoginScreen = destination.id != R.id.login_fragment
            }
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    /**
     * Go to the login screen if the user logs out and current screen is not splash / login.
     */
    private fun observeUserLoginStatus() {
        userViewModel.isLoggedIn.observe(this) { isLoggedIn ->
            val notSplashScreen =
                navController.graph.startDestination != navController.currentDestination?.id
            if (isLoggedIn == false && notSplashScreen && notLoginScreen) {
                showToast(getString(R.string.session_end))
                navController.navigate(NavGraphDirections.showAuthFlow())
            }
        }
    }
}
