package com.rocketinsights.android.work.messages

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rocketinsights.android.repos.MessageRepository
import retrofit2.HttpException
import timber.log.Timber

private const val WORK_STARTED = "Messages update work started."
private const val WORK_FAILED_HTTP = "Messages update work failed with HTTP exception."
private const val WORK_FAILED = "Messages update work failed with unknown exception."

class MessagesUpdateWorker(
    private val messageRepository: MessageRepository,
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            Timber.d(WORK_STARTED)
            messageRepository.refreshMessages()
        } catch (e: HttpException) {
            Timber.e(e, WORK_FAILED_HTTP)
            return Result.retry()
        } catch (e: Throwable) {
            Timber.e(e, WORK_FAILED)
            return Result.failure()
        }
        return Result.success()
    }
}