package com.rocketinsights.android.ui.common

import android.content.Context

class StringResProviderImpl(private val context: Context) : StringResProvider {

    override fun getString(resId: Int): String = context.getString(resId)
}