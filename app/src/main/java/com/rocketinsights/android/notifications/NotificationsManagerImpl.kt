package com.rocketinsights.android.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import com.rocketinsights.android.R
import java.util.UUID

abstract class NotificationsManagerImpl(
    val context: Context
) : NotificationsManager {
    companion object {
        // If some Channel Configuration it's changed please update the ChannelId since Android SO
        // always keep the original configuration unless the user uninstall and install the application.
        private const val CHANNEL_ID_DEFAULT = "Notifications-Default"
    }

    protected val notificationManager: NotificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    // This Map is used to save [when] value for notifications to prevent them to switch places
    private val notificationsIdWhen = HashMap<Int, Long>()

    protected val defaultTitle by lazy {
        context.getString(R.string.app_name)
    }

    private val notificationIcon: Int
        get() = R.mipmap.ic_launcher

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationsChannels()
        }
    }

    abstract fun getDefaultPendingIntent(): PendingIntent

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected open fun createNotificationsChannels() {
        createDefaultChannel()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createDefaultChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID_DEFAULT,
            defaultTitle,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("CheckResult")
    private fun sendNotification(notificationId: Int, notification: Notification) {
        notification.`when` = getWhenFromNotificationId(notificationId)

        // In the future check if user has notifications enable on in-app settings
        notificationManager.notify(notificationId, notification)
    }

    private fun cancelNotification(notificationId: Int) {
        if (notificationsIdWhen.containsKey(notificationId)) {
            notificationsIdWhen.remove(notificationId)
        }

        notificationManager.cancel(notificationId)
    }

    @Suppress("DEPRECATION")
    protected fun createBaseNotification(channel: String) = Notification.Builder(context)
        .setSmallIcon(notificationIcon)
        .setColor(context.getColor(R.color.colorPrimary))
        .setPriority(Notification.PRIORITY_MAX)
        .setAutoCancel(true)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setChannelId(channel)
            }
        }

    override fun createNotification(
        channelId: String?,
        title: String?,
        description: String?,
        intent: PendingIntent?
    ) = createBaseNotification(channelId ?: CHANNEL_ID_DEFAULT)
        .apply {
            if (title != null) {
                setContentTitle(title)
            }

            if (description != null) {
                setContentText(description)
            }

            setContentIntent(intent ?: getDefaultPendingIntent())

        }.build()

    private fun getWhenFromNotificationId(notificationId: Int): Long {
        if (!notificationsIdWhen.containsKey(notificationId)) {
            notificationsIdWhen[notificationId] = System.currentTimeMillis()
        }

        return notificationsIdWhen[notificationId]!!
    }

    private fun getRandomId(): Int = UUID.randomUUID().toString().hashCode()

    override fun showNotification(id: Int?, title: String?, description: String?) {
        sendNotification(
            id ?: getRandomId(),
            createNotification(title ?: defaultTitle, description)
        )
    }

    override fun showNotification(id: Int?, notification: Notification) {
        sendNotification(id ?: getRandomId(), notification)
    }

    override fun clearNotification(id: Int) {
        try {
            cancelNotification(id)
        } catch (e: Exception) {
            // Nothing
        }
    }

    override fun clearAll() {
        notificationManager.cancelAll()
    }
}
