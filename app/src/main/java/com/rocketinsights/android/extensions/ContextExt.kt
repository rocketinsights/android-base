package com.rocketinsights.android.extensions

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    makeText(this, message, duration).show()
}