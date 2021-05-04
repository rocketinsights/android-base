package com.rocketinsights.android.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.annotation.IdRes
import androidx.navigation.NavDeepLinkBuilder
import com.rocketinsights.android.R

/**
 * Animations widget provides shortcuts to animation screens.
 */
class AnimationWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // there may be multiple widgets active, so update all of them
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
    // create deep link PendingIntents
    val deepLinkBuilder = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)

    val pendingIntentAnimations =
        getDeepLinkPendingIntent(deepLinkBuilder, R.id.animations_fragment)
    val pendingIntentLottie =
        getDeepLinkPendingIntent(deepLinkBuilder, R.id.account_setup_animation_fragment)
    val pendingIntentProperty =
        getDeepLinkPendingIntent(deepLinkBuilder, R.id.property_animation_fragment)
    val pendingIntentContainer =
        getDeepLinkPendingIntent(deepLinkBuilder, R.id.container_transform_fragment)

    // construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.animation_widget).apply {
        setOnClickPendingIntent(R.id.animation_widget_container, pendingIntentAnimations)
        setOnClickPendingIntent(R.id.button_widget_lottie, pendingIntentLottie)
        setOnClickPendingIntent(R.id.button_widget_property, pendingIntentProperty)
        setOnClickPendingIntent(R.id.button_widget_container, pendingIntentContainer)
    }

    // instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun getDeepLinkPendingIntent(
    navDeepLinkBuilder: NavDeepLinkBuilder,
    @IdRes destinationId: Int
) = navDeepLinkBuilder
    .setDestination(destinationId)
    .createPendingIntent()
