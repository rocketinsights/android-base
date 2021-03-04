package com.rocketinsights.android.extensions

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Returns activity binding property delegate, which may be used between onCreate and onDestroy (inclusive).
 */
inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline inflate: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        inflate(layoutInflater)
    }

/**
 * Returns fragment binding property delegate, which may be used between onViewCreated and onDestroyView (inclusive).
 */
fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: bind(requireView()).also {
                // cache it when the view isn't destroyed
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }
