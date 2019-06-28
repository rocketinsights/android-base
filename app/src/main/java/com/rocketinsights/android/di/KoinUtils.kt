package com.rocketinsights.android.di

import android.app.Application
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.ui.MainFragment
import com.rocketinsights.android.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        modules(listOf(networkModule(), repositoryModule(), scopeModules(this@initKoin)))
    }
}

private fun networkModule() = module {
    single { ApiService.getApiService() }
}

private fun repositoryModule() = module {
    single { MessageRepository(get()) }
}

private fun scopeModules(application: Application) = module {
    scope(named<MainFragment>()) {
        viewModel { MainViewModel(application, get()) }
    }
}