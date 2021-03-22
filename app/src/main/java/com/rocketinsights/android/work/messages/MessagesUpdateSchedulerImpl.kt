package com.rocketinsights.android.work.messages

import androidx.work.WorkRequest
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.work.Work
import kotlinx.coroutines.withContext

private const val WORK_NAME = "com.rocketinsights.android.work.MessagesUpdate"

class MessagesUpdateSchedulerImpl(
    private val workRequest: WorkRequest,
    private val work: Work,
    private val dispatcherProvider: DispatcherProvider
) : MessagesUpdateScheduler {
    override suspend fun scheduleBackgroundUpdates() = withContext(dispatcherProvider.io()) {
        work.scheduleUniqueWork(WORK_NAME, workRequest)
    }

    override suspend fun cancelBackgroundUpdates() = withContext(dispatcherProvider.io()) {
        work.cancelUniqueWork(WORK_NAME)
    }
}
