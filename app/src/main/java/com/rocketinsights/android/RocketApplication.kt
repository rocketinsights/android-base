package com.rocketinsights.android

import android.app.Application
import com.rocketinsights.android.di.initKoin
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class RocketApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        initKoin()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // uncomment this and the below CrashlyticsTree class out if using Fabric/Crashlytics
//        else {
//            Fabric.with(this, Crashlytics(), Answers())
//            Timber.plant(CrashlyticsTree())
//        }
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