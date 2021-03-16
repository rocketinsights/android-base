package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentPhotoBinding
import com.rocketinsights.android.extensions.viewBinding

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    private val binding by viewBinding(FragmentPhotoBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }
}