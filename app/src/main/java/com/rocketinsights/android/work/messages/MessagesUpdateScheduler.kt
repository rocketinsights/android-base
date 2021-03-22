package com.rocketinsights.android.work.messages

interface MessagesUpdateScheduler {

    suspend fun scheduleBackgroundUpdates()

    suspend fun cancelBackgroundUpdates()
}