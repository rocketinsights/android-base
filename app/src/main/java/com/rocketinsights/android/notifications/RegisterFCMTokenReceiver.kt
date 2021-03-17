package com.rocketinsights.android.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessaging
import com.rocketinsights.android.network.ApiService
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterFCMTokenReceiver : BroadcastReceiver() {
    private val myHelper: RegisterFCMTokenReceiverHelper by lazy { RegisterFCMTokenReceiverHelper() }

    companion object {
        fun broadcast(context: Context) {
            context.sendBroadcast(Intent(context, RegisterFCMTokenReceiver::class.java))
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        myHelper.onReceive(context, intent)
    }
}

@OptIn(KoinApiExtension::class)
class RegisterFCMTokenReceiverHelper : KoinComponent {
    private val apiService: ApiService by inject()

    fun onReceive(context: Context, intent: Intent) {
        // Register the FCM Token in our API Like:
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            runBlocking {
                // apiService.setFCMToken(it)
            }
        }
    }
}
