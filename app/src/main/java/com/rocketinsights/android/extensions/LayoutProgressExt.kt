package com.rocketinsights.android.extensions

import com.rocketinsights.android.databinding.LayoutProgressBinding

private const val FADE_DURATION = 500L

fun LayoutProgressBinding.show() {
    root.fadeIn(duration = FADE_DURATION)
}

fun LayoutProgressBinding.hide() {
    root.fadeOut(duration = FADE_DURATION)
}
