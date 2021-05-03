package com.rocketinsights.android.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import com.rocketinsights.android.R

/**
 * Animations widget is a shortcut to animations screen.
 */
class AnimationWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

private fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.animations_fragment)
        .createPendingIntent()

    val widgetText = context.getString(R.string.appwidget_text)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.animation_widget).apply {
        setTextViewText(R.id.appwidget_text, widgetText)
        setOnClickPendingIntent(R.id.animation_widget_container, pendingIntent)
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
