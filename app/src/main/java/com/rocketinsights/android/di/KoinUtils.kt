package com.rocketinsights.android.di

import android.app.Application
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.coroutines.DispatcherProviderImpl
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val API_BASE_URL = "http://www.mocky.io/v2/"

fun Application.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        modules(listOf(networkModule(), repositoryModule(), scopeModules()))
    }
}

private fun networkModule() = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}

private fun repositoryModule() = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
    single { MessageRepository(get(), get()) }
}

private fun scopeModules() = module {
    viewModel { MainViewModel(get()) }
}