package com.rocketinsights.android.ui.common

import androidx.annotation.StringRes

interface StringResProvider {

    fun getString(@StringRes resId: Int): String
}