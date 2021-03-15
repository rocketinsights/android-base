package com.rocketinsights.android.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.coroutines.DispatcherProviderImpl
import com.rocketinsights.android.db.Database
import com.rocketinsights.android.managers.InternetManager
import com.rocketinsights.android.managers.PermissionsManager
import com.rocketinsights.android.managers.PermissionsManagerImpl
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.viewmodels.ConnectivityViewModel
import com.rocketinsights.android.viewmodels.MainViewModel
import com.rocketinsights.android.viewmodels.MessagesViewModel
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val API_BASE_URL = "https://run.mocky.io/v3/"

fun Application.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        modules(listOf(
            networkModule(),
            databaseModule(),
            managersModule(),
            repositoryModule(),
            scopeModules())
        )
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

private fun databaseModule() = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            Database.NAME
        ).build()
    }

    single { get<Database>().messageDao() }
}

private fun managersModule() = module {
    single {
        InternetManager(
            androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }
    factory<PermissionsManager> { PermissionsManagerImpl(get()) }
}

private fun repositoryModule() = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
    single { MessageRepository(get(), get(), get()) }
}

private fun scopeModules() = module {
    viewModel { MainViewModel(get()) }
    viewModel { MessagesViewModel(get()) }
    viewModel { ConnectivityViewModel(get()) }
    viewModel { PermissionsViewModel(get()) }
}
