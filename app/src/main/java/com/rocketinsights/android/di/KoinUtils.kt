package com.rocketinsights.android.di

import android.app.Application
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.coroutines.DispatcherProviderImpl
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.ui.common.StringResProvider
import com.rocketinsights.android.ui.common.StringResProviderImpl
import com.rocketinsights.android.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun Application.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        modules(listOf(networkModule(), repositoryModule(), scopeModules()))
    }
}

private fun networkModule() = module {
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ApiService::class.java)
    }
}

private fun repositoryModule() = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
    single { MessageRepository(get(), get()) }
}

private fun scopeModules() = module {
    single<StringResProvider> { StringResProviderImpl(get()) }
    viewModel { MainViewModel(get(), get()) }
}