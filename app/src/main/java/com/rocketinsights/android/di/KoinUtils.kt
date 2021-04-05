package com.rocketinsights.android.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.rocketinsights.android.auth.AuthManager
import com.rocketinsights.android.auth.FirebaseAuthManager
import com.rocketinsights.android.auth.SessionStorage
import com.rocketinsights.android.auth.SessionWatcher
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.coroutines.DispatcherProviderImpl
import com.rocketinsights.android.db.Database
import com.rocketinsights.android.managers.InternetManager
import com.rocketinsights.android.managers.PermissionsManager
import com.rocketinsights.android.managers.PermissionsManagerImpl
import com.rocketinsights.android.managers.location.LocationManager
import com.rocketinsights.android.managers.location.LocationManagerImpl
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.network.NetworkingManager
import com.rocketinsights.android.prefs.AuthLocalStore
import com.rocketinsights.android.prefs.AuthLocalStoreImpl
import com.rocketinsights.android.prefs.LocalStore
import com.rocketinsights.android.prefs.LocalStoreImpl
import com.rocketinsights.android.repos.AuthRepository
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.ui.MainActivity
import com.rocketinsights.android.ui.MainFragment
import com.rocketinsights.android.ui.ParentScrollProvider
import com.rocketinsights.android.viewmodels.ConnectivityViewModel
import com.rocketinsights.android.viewmodels.LocationViewModel
import com.rocketinsights.android.viewmodels.MainViewModel
import com.rocketinsights.android.viewmodels.MessagesViewModel
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import com.rocketinsights.android.viewmodels.PhotoViewModel
import com.rocketinsights.android.viewmodels.SessionViewModel
import com.rocketinsights.android.viewmodels.UserViewModel
import com.rocketinsights.android.work.Work
import com.rocketinsights.android.work.WorkImpl
import com.rocketinsights.android.work.messages.MessagesUpdateScheduler
import com.rocketinsights.android.work.messages.MessagesUpdateSchedulerImpl
import com.rocketinsights.android.work.messages.MessagesUpdateWorkRequestFactory
import com.rocketinsights.android.work.messages.MessagesUpdateWorker
import get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.dsl.module

@KoinExperimentalAPI
fun Application.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        workManagerFactory()
        modules(
            listOf(
                networkModule(),
                databaseModule(),
                managersModule(),
                repositoryModule(),
                authModule(),
                viewModelsModule(),
                viewInteractorsModule(),
                workModule()
            )
        )
    }
}

private fun networkModule() = module {
    single {
        NetworkingManager(get(), get(), get())
    }
    single<ApiService> {
        get<NetworkingManager>().retrofitInstance.create(ApiService::class.java)
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
    single<LocationManager> { LocationManagerImpl(get()) }
}

private fun repositoryModule() = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
    single { MessageRepository(get(), get(), get()) }
    single {
        AuthRepository(get(), get(), get(), get()).apply {
            get<NetworkingManager>().sessionWatcher = this
        }
    }
}

private fun authModule() = module {
    single { FirebaseAuth.getInstance() }
    single<LocalStore> { LocalStoreImpl(get()) }
    single<AuthLocalStore> { AuthLocalStoreImpl(get()) }
    factory<AuthManager> { (context: Context) ->
        FirebaseAuthManager(context, get(), AuthUI.getInstance())
    }
}

private fun viewModelsModule() = module {
    viewModel {
        SessionViewModel(
            get(clazz = AuthRepository::class.java) as SessionWatcher,
            get(clazz = NetworkingManager::class.java) as SessionStorage
        )
    }
    viewModel { MainViewModel(get()) }
    viewModel { MessagesViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { ConnectivityViewModel(get()) }
    viewModel { PermissionsViewModel(get()) }
    viewModel { PhotoViewModel() }
    viewModel { LocationViewModel(get(), get()) }
}

private fun viewInteractorsModule() = module {
    single { ParentScrollProvider() }
}

private fun workModule() = module {
    worker { MessagesUpdateWorker(get(), get(), get()) }
    single { MessagesUpdateWorkRequestFactory.createWorkRequest() }
    single { WorkManager.getInstance(get()) }
    single<Work> { WorkImpl(get()) }
    single<MessagesUpdateScheduler> { MessagesUpdateSchedulerImpl(get(), get(), get()) }
}
