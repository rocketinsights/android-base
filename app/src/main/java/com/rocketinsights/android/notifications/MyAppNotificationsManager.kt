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
    context: Context,
    notificationManager: NotificationManager
) : BaseNotificationsManager(context, notificationManager) {
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
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun createNotificationsChannels() {
        super.createNotificationsChannels()
        // Create all the channels we need and then use their ID when creating Notifications, for example:
        createNewsChannel()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNewsChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID_NEWS,
            "$defaultTitle - News",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}
