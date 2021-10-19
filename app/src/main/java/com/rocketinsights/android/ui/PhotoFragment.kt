package com.rocketinsights.android.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.transition.MaterialFadeThrough
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentPhotoBinding
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.common.BaseFragment
import com.rocketinsights.android.viewmodels.PhotoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Photo fragment shows the photo taken by the camera app.
 * It's purpose is to show the result of TakePicture action started on the previous fragment.
 */
class PhotoFragment : BaseFragment(R.layout.fragment_photo) {

    private val binding by viewBinding(FragmentPhotoBinding::bind)
    private val photoViewModel: PhotoViewModel by sharedViewModel()

    override fun doOnAttach(context: Context) {
        setupOnBackPressed()
    }

    override fun doOnCreate(savedInstanceState: Bundle?) {
        setScreenTransitions()
    }

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        updateUI()
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

    private fun setScreenTransitions() {
        enterTransition = MaterialFadeThrough()
    }
}
