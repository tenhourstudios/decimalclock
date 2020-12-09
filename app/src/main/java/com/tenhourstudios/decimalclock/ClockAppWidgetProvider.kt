package com.tenhourstudios.decimalclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews


class ClockAppWidgetProvider : AppWidgetProvider() {

    private var service: PendingIntent? = null
    private val TAG = "ClockAppWidgetProvider"

    override fun onEnabled(context: Context) {
        Log.d(TAG, "Entering onEnabled")
        super.onEnabled(context)
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ClockUpdateService::class.java)
        context.startForegroundService(intent) // loads text immediately after placing widget

        if (service == null) {
            service = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
        manager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            43200,
            service
        )
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "Entering onUpdate")
        val pendingIntent = Intent(context, MainActivity::class.java)
            .let {
                    clickIntent ->  PendingIntent.getActivity(context, 0, clickIntent, 0)
            }
        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(
                context.packageName,
                R.layout.widget_clock_layout
            ).apply {
                setOnClickPendingIntent(R.id.widget_text, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onDisabled(context: Context) {
        Log.d(TAG, "Entering onDisabled")
        val intent = Intent(context, ClockUpdateService::class.java)
        context.stopService(intent)
        super.onDisabled(context)
    }
}


