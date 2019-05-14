package com.rocketinsights.android.ext

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun <T> T.toDeferred() = GlobalScope.async { this@toDeferred }