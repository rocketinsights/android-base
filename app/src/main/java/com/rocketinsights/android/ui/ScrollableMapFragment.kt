package com.rocketinsights.android.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.maps.SupportMapFragment

/**
 * This class provides an implementation to use Google Maps and [android.widget.ScrollView] in the same layout.
 * [android.widget.ScrollView] behaviour should be disabled using [setOnMapTouchListener].
 */
class ScrollableMapFragment : SupportMapFragment() {
    private var touchListener: () -> Unit = {}

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstance: Bundle?): View? {
        val layout = super.onCreateView(layoutInflater, viewGroup, savedInstance) as ViewGroup?
            ?: return null

        val frameLayout = TouchableWrapper(requireContext())
        frameLayout.setBackgroundColor(requireContext().getColor(android.R.color.transparent))
        layout.addView(frameLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT))

        return layout
    }

    fun setOnMapTouchListener(listener: () -> Unit) {
        this.touchListener = listener
    }

    private inner class TouchableWrapper(context: Context) : FrameLayout(context) {
        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchListener()
                MotionEvent.ACTION_UP -> touchListener()
            }

            return super.dispatchTouchEvent(event)
        }
    }
}