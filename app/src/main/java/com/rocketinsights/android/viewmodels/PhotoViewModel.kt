package com.rocketinsights.android.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import java.io.File

/**
 * Shared view model for handling temp image file.
 */
class PhotoViewModel : ViewModel() {

    var imageFile: File? = null
    var imageUri: Uri? = null

    fun deletePhoto() {
        imageFile?.delete()
        imageFile = null
        imageUri = null
    }

    override fun onCleared() {
        deletePhoto()
        super.onCleared()
    }
}
