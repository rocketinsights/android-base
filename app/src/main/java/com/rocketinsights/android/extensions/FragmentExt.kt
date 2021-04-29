package com.rocketinsights.android.extensions

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

fun Fragment.setupActionBar(toolbar: Toolbar) {
    val activity = requireActivity() as AppCompatActivity
    activity.setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener { view ->
        view.findNavController().navigateUp()
    }
}

fun Fragment.showSystemUI(root: View) {
    WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
    WindowInsetsControllerCompat(requireActivity().window, root).run {
        show(WindowInsetsCompat.Type.navigationBars())
    }
}

fun Fragment.hideSystemUI(root: View) {
    WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
    WindowInsetsControllerCompat(requireActivity().window, root).run {
        hide(WindowInsetsCompat.Type.navigationBars())
    }
}
