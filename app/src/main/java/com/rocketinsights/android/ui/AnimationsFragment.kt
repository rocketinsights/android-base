package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.rocketinsights.android.R

/**
 * Animations fragment contains cards which can be used to launch different animations.
 * This screen also runs ConstraintSet animation on cards and subtitle by using MotionLayout.
 */
class AnimationsFragment : Fragment(R.layout.fragment_animations) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }
}
