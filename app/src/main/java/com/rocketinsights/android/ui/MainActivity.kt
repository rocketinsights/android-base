package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.ActivityMainBinding
import com.rocketinsights.android.extensions.viewBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val parentScrollProvider: ParentScrollProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()

        parentScrollProvider.registerEnablementOfParentScrollFunction {
            binding.scrollView.requestDisallowInterceptTouchEvent(it)
        }
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
}
