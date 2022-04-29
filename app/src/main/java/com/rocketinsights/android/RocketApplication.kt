package com.rocketinsights.android

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.rocketinsights.android.di.koin.initKoin
import com.rocketinsights.android.work.messages.MessagesUpdateScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

@HiltAndroidApp
class RocketApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val messagesUpdateScheduler: MessagesUpdateScheduler by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin()
        applicationScope.launch {
            init()
            scheduleWork()
        }
    }

    // We wouldn't normally override this method.
    // This is just an example of calling a method for cancelling background work.
    // Move this call to your app settings screen or menu where it would be possible to toggle background updates.
    override fun onTerminate() {
        applicationScope.launch {
            cancelWork()
        }
        super.onTerminate()
    }

    private fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // uncomment this and the below CrashlyticsTree class out if using Fabric/Crashlytics
//        else {
//            Fabric.with(this, Crashlytics(), Answers())
//            Timber.plant(CrashlyticsTree())
//        }
    }

    private suspend fun scheduleWork() {
        messagesUpdateScheduler.scheduleBackgroundUpdates()
    }

    private suspend fun cancelWork() {
        messagesUpdateScheduler.cancelBackgroundUpdates()
    }

//    private class CrashlyticsTree : Timber.Tree() {
//        private val priorityKey = "priority"
//        private val tagKey = "tag"
//        private val messageKey = "message"
//
//        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
//                return
//            }
//
//            Crashlytics.setInt(priorityKey, priority)
//            Crashlytics.setString(tagKey, tag)
//            Crashlytics.setString(messageKey, message)
//
//            if (t == null) {
//                Crashlytics.logException(Exception(message))
//            } else {
//                Crashlytics.logException(t)
//            }
//        }
//    }
}
