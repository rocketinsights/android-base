package com.rocketinsights.android.ui

import android.os.Bundle
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.ActivityMainBinding
import com.rocketinsights.android.extensions.viewBinding
import org.koin.androidx.scope.ScopeActivity

class MainActivity : ScopeActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

//    private lateinit var navController: NavController
//    private val authManager: AuthManager by inject(parameters = { parametersOf(this@MainActivity) })
//    private val sessionViewModel: SessionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(binding.root)

//        navController =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()

//        listenSessionViewModelEvents()
    }

//    private fun listenSessionViewModelEvents() {
//        sessionViewModel.sessionDataSate.observe(
//            this,
//            { event ->
//                event.getContentIfNotHandled()?.let {
//                    when (it) {
//                        SessionDataState.CLEARING -> {
//                            // TODO Show Progress would be good
//                        }
//                        SessionDataState.CLEARED -> {
//                            // User Logout
//                            // TODO Hide Progress showed when CLEARING
//                            showToast(getString(R.string.session_end))
//                            authManager.launchSignInFlow()
//                        }
//                    }
//                }
//            }
//        )
//    }
}
