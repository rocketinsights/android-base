package com.rocketinsights.android.work

import androidx.work.WorkRequest
import java.util.UUID

interface Work {

    fun schedule(workRequest: WorkRequest)

    fun scheduleUniqueWork(uniqueWorkName: String, workRequest: WorkRequest)

    fun cancel(workId: UUID)

    fun cancelUniqueWork(uniqueWorkName: String)
}
