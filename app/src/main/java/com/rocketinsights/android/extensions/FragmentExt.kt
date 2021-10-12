package com.rocketinsights.android.extensions

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rocketinsights.android.ui.components.DialogBuilder

fun Fragment.setupActionBar(toolbar: Toolbar) {
    val activity = requireActivity() as AppCompatActivity
    activity.setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener { view ->
        view.findNavController().navigateUp()
    }
}

fun Fragment.showDialog(setup: DialogBuilder.() -> Unit) =
    DialogBuilder(requireContext(), setup = setup).build().show()

fun Fragment.showNavBar(root: View) {
    WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
    WindowInsetsControllerCompat(requireActivity().window, root).run {
        show(WindowInsetsCompat.Type.navigationBars())
    }
}

fun Fragment.hideNavBar(root: View) {
    WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
    WindowInsetsControllerCompat(requireActivity().window, root).run {
        hide(WindowInsetsCompat.Type.navigationBars())
    }
}

fun Fragment.hideKeyboard(target: View? = null) = requireActivity().hideKeyboard(target)

fun Fragment.showKeyboard(target: View? = null) = requireActivity().showKeyboard(target)
