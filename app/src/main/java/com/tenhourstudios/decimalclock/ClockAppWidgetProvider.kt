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

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        Log.d(TAG, "Entering onUpdate")
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

        val pendingIntent = Intent(context, MainActivity::class.java)
            .let {
                    intent ->  PendingIntent.getActivity(context, 0, intent, 0)
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
}


