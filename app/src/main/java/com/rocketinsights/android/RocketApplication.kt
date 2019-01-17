package com.rocketinsights.android

import android.app.Application
import com.rocketinsights.android.viewmodels.MainViewModel
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import timber.log.Timber

class RocketApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(module {
            viewModel { MainViewModel(this@RocketApplication) }
        }))

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}