package com.rocketinsights.android.work.messages

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

object MessagesUpdateWorkRequestFactory {

    fun createWorkRequest(): WorkRequest {
        val repeatInterval = 2L
        val repeatUnit = TimeUnit.HOURS
        val initialDelay = 10L
        val delayUnit = TimeUnit.SECONDS
        val backoffDelay = 10L
        val backoffDelayUnit = TimeUnit.MINUTES

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        return PeriodicWorkRequestBuilder<MessagesUpdateWorker>(
            repeatInterval,
            repeatUnit
        ).setConstraints(constraints)
            .setInitialDelay(initialDelay, delayUnit)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, backoffDelay, backoffDelayUnit)
            .build()
    }
}
