package com.rocketinsights.android.extensions

import android.animation.Animator
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver

private const val FADE_IN_DURATION = 500L
private const val FADE_OUT_DURATION = 500L

fun View.show() {
    if (visibility == VISIBLE) return
    visibility = VISIBLE
}

fun View.hide() {
    if (visibility == GONE) return
    visibility = GONE
}

fun View.invisible() {
    if (visibility == INVISIBLE) return
    visibility = INVISIBLE
}

fun View.show(show: Boolean) {
    if (show) {
        show()
    } else {
        hide()
    }
}

fun View.invisible(invisible: Boolean) {
    if (invisible) {
        invisible()
    } else {
        show()
    }
}

fun View.isVisible() = visibility == VISIBLE

fun View.isGone() = visibility == GONE

fun View.isInvisible() = visibility == INVISIBLE

fun View.enable() {
    if (isEnabled) return
    isEnabled = true
}

fun View.disable() {
    if (!isEnabled) return
    isEnabled = false
}

fun View.enable(enable: Boolean) {
    if (enable) enable()
    else disable()
}

fun View.fadeIn(
    duration: Long = FADE_IN_DURATION,
    listener: Animator.AnimatorListener? = null
) {
    if (isVisible()) return
    alpha = 0F
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
    if (!isVisible()) return
    animate()
        .alpha(0F)
        .setDuration(duration)
        .setListener(listener)
        .withEndAction { hide() }
}

inline fun View.afterMeasured(crossinline action: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            if (measuredWidth > 0 && measuredHeight > 0) {
                action()
            }
        }
    })
}
