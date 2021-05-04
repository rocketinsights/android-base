package com.rocketinsights.android.extensions

import android.os.Build
import java.time.Year
import java.util.Calendar

// Add Date Extensions Here

object DateUtils {
    fun currentYear() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Year.now().value
    } else {
        Calendar.getInstance().get(Calendar.YEAR)
    }
}