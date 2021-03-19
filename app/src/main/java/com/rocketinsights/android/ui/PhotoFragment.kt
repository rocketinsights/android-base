package com.rocketinsights.android.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentPhotoBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.viewmodels.PhotoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    private val binding by viewBinding(FragmentPhotoBinding::bind)
    private val photoViewModel: PhotoViewModel by sharedViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menu_items_group, false)
    }

    private fun setupOnBackPressed() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun onBackPressed() {
        photoViewModel.deletePhoto()
        findNavController().popBackStack()
    }

    private fun updateUI() {
        binding.imageCameraShot.load(photoViewModel.imageUri)
    }
}