package com.rocketinsights.android.notifications

import android.app.Notification
import android.app.PendingIntent

interface NotificationsManager {
    fun createNotification(
        channelId: String? = null,
        title: String? = null,
        description: String? = null,
        intent: PendingIntent? = null
    ): Notification

    fun showNotification(id: Int? = null, title: String?, description: String? = null)

    fun showNotification(id: Int? = null, notification: Notification)

    fun clearNotification(id: Int)

    fun clearAll()
}
