package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.rocketinsights.android.R
import com.rocketinsights.android.auth.AuthManager
import com.rocketinsights.android.databinding.ActivityMainBinding
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.SessionDataState
import com.rocketinsights.android.viewmodels.SessionViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val authManager: AuthManager by inject(parameters = { parametersOf(this@MainActivity) })

    private val sessionViewModel: SessionViewModel by viewModel()

    private val parentScrollProvider: ParentScrollProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()

        parentScrollProvider.registerEnablementOfParentScrollFunction {
            binding.scrollView.requestDisallowInterceptTouchEvent(it)
        }

        listenSessionViewModelEvents()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // to trigger OnBackPressedCallbacks in your fragments
        return navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun navigateUp() =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    private fun listenSessionViewModelEvents() {
        sessionViewModel.sessionDataSate.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    SessionDataState.CLEARING -> {
                        // TODO Show Progress would be good
                    }
                    SessionDataState.CLEARED -> {
                        // User Logout
                        // TODO Hide Progress showed when CLEARING
                        showToast(getString(R.string.session_end))
                        authManager.launchSignInFlow()
                    }
                }
            }
        })
    }
}
