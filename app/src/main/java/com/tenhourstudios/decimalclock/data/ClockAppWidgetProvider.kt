package com.tenhourstudios.decimalclock.data

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import androidx.preference.PreferenceManager
import com.tenhourstudios.decimalclock.R
import timber.log.Timber
import java.time.OffsetTime


class ClockAppWidgetProvider : AppWidgetProvider() {

    val ACTION_AUTO_UPDATE = "UPDATE_WIDGET_TIME"

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Timber.i("${intent.action} received")
        if (intent.action == ACTION_AUTO_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidgetComponentName = ComponentName(context.packageName, javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
            if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                Timber.i("Calling onUpdate from onReceive")
                this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds)
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pendingIntent = NavDeepLinkBuilder(context).setGraph(R.navigation.navigation_main)
            .setDestination(R.id.clockFragment).createPendingIntent()

        val widgetText = updateTime(context)
        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(
                context.packageName,
                R.layout.widget_clock_layout
            ).apply {
                setOnClickPendingIntent(R.id.widget_text, pendingIntent)
                setTextViewText(R.id.widget_text, widgetText)
                setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, 72F)
            }
            Timber.i("Update: $widgetText")
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_text)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        Timber.i("onEnabled")
        // start alarm
        super.onEnabled(context)
        val appWidgetAlarm = ClockAppWidgetAlarm(context.applicationContext)
        appWidgetAlarm.startAlarm()
    }

    override fun onDisabled(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName = ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        if (appWidgetIds.isEmpty()) {
            // stop alarm
            val appWidgetAlarm = ClockAppWidgetAlarm(context.applicationContext)
            appWidgetAlarm.stopAlarm()
        }
    }

    private fun updateTime(context: Context): String {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val format = when (sharedPrefs.getString(
            "widget_style",
            context.getString(R.string.prefs_digital_standard)
        )) {
            context.getString(R.string.prefs_digital_decimal) -> KEY_DECIMAL
            context.getString(R.string.prefs_digital_percentage) -> KEY_PERCENTAGE
            else -> KEY_STANDARD
        }
        val timeZoneOffset = OffsetTime.now().offset
        val millisToday =
            (System.currentTimeMillis() + 1000 * timeZoneOffset.totalSeconds) % MILLIS_IN_A_DAY
        val time = Clock(millisToday, false)
        val widgetText = time.tenHourTime(format, KEY_PRECISION_LOW)

        Timber.i(widgetText)
        return widgetText
    }
}


