package com.rocketinsights.android.extensions

import android.animation.Animator
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

private const val FADE_IN_DURATION = 300L
private const val FADE_OUT_DURATION = 300L

fun View.show() {
    if (visibility == VISIBLE) return
    visibility = VISIBLE
}

fun View.hide() {
    if (visibility == INVISIBLE) return
    visibility = INVISIBLE
}

fun View.remove() {
    if (visibility == GONE) return
    visibility = GONE
}

fun View.enable() {
    if (isEnabled) return
    isEnabled = true
}

fun View.disable() {
    if (!isEnabled) return
    isEnabled = false
}

fun View.fadeIn(
    duration: Long = FADE_IN_DURATION,
    listener: Animator.AnimatorListener? = null
) {
    show()
    animate()
        .alpha(1F)
        .setDuration(duration)
        .setListener(listener)
}

fun View.fadeOut(
    duration: Long = FADE_OUT_DURATION,
    listener: Animator.AnimatorListener? = null
) {
    animate()
        .alpha(0F)
        .setDuration(duration)
        .setListener(listener)
        .withEndAction { hide() }
}
