package com.rocketinsights.android.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.repos.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_MESSAGE = "message"
    }

    private val notificationsManager: MyAppNotificationsManager by inject()
    private val authRepository: AuthRepository by inject()
    private val dispatcher: DispatcherProvider by inject()
    private val scope = CoroutineScope(dispatcher.io())

    // Override handle intent to use our custom Notification Manager
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.tag("onPushNotification")
            .d("Notification: ${remoteMessage.notification} | Custom Data:  ${remoteMessage.data}")

        var title: String? = null
        var description: String? = null

        if (remoteMessage.notification != null) { // Comes from Firebase Console
            title = remoteMessage.notification?.title
            description = remoteMessage.notification?.body
        } else {
            remoteMessage.data[EXTRA_TITLE]?.let {
                title = it
            }
            remoteMessage.data[EXTRA_MESSAGE]?.let {
                description = it
            }
        }

        // If no title it's send, use description as title
        if (title == null && description != null) {
            title = description
            description = null
        }

        notificationsManager.showNotification(title = title, description = description)
    }

    override fun onNewToken(token: String) {
        Timber.d("New Firebase Token: %s", token)

        // Register Token in our API
        scope.launch {
            authRepository.registerNotificationsToken(token)
        }

        super.onNewToken(token)
    }
}
