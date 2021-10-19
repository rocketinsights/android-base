package com.rocketinsights.android.ui.common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Base fragment is a lightweight parent class of all Fragments.
 * Purpose of this class is to reduce boilerplate code and enforce some rules.
 * WARNING: Keep this class lightweight. Do not add functions that can be extension functions.
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    final override fun onAttach(context: Context) {
        super.onAttach(context)
        doOnAttach(context)
    }

    protected open fun doOnAttach(context: Context) {}

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doOnCreate(savedInstanceState)
    }

    protected open fun doOnCreate(savedInstanceState: Bundle?) {}

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doOnViewCreated(view, savedInstanceState)
    }

    protected open fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {}

    // use SavedStateHandle in a ViewModel
    final override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    final override fun onStart() {
        super.onStart()
        doOnStart()
    }

    protected open fun doOnStart() {}

    final override fun onResume() {
        super.onResume()
        doOnResume()
    }

    protected open fun doOnResume() {}

    final override fun onPause() {
        doOnPause()
        super.onPause()
    }

    protected open fun doOnPause() {}

    override fun onStop() {
        doOnStop()
        super.onStop()
    }

    protected open fun doOnStop() {}

    // use SavedStateHandle in a ViewModel
    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    final override fun onDestroyView() {
        doOnDestroyView()
        super.onDestroyView()
    }

    protected open fun doOnDestroyView() {}

    final override fun onDestroy() {
        doOnDestroy()
        super.onDestroy()
    }

    protected open fun doOnDestroy() {}

    final override fun onDetach() {
        doOnDetach()
        super.onDetach()
    }

    protected open fun doOnDetach() {}
}
