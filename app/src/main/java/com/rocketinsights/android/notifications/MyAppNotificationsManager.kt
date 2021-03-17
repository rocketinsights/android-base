package com.rocketinsights.android.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.rocketinsights.android.ui.MainActivity

class MyAppNotificationsManager(
    context: Context
) : NotificationsManagerImpl(context) {
    companion object {
        // If some Channel Configuration it's changed please update the ChannelId since Android SO
        // always keep the original configuration unless the user uninstall and install the application.
        private const val CHANNEL_ID_NEWS = "Notifications-News"
    }

    override fun getDefaultPendingIntent(): PendingIntent {
        val notificationIntent = Intent(context, MainActivity::class.java)

        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            notificationIntent,
            0
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun createNotificationsChannels() {
        super.createNotificationsChannels()
        // Create all the channels we need
        createChatChannel()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChatChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID_NEWS,
            "$defaultTitle - News",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}
