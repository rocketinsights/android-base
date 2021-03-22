package com.rocketinsights.android.extensions

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.rocketinsights.android.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val IMAGE_DATE_PATTERN = "yyyyMMdd_HHmmss"

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat(IMAGE_DATE_PATTERN, Locale.US).format(Date())
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}

fun Context.getUriForFile(file: File): Uri =
    FileProvider.getUriForFile(this, getString(R.string.fileProvider), file)