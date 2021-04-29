package com.rocketinsights.android.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

fun Fragment.setupActionBar(toolbar: Toolbar) {
    val activity = requireActivity() as AppCompatActivity
    activity.setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener { view ->
        view.findNavController().navigateUp()
    }
}
