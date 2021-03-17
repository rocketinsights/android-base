package com.rocketinsights.android.notifications

import com.google.firebase.messaging.FirebaseMessaging

object FirebaseUtils {
    fun getPushNotificationsToken(): String = FirebaseMessaging.getInstance().token.result
}
