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

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, ClockUpdateService::class.java)
        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT)
        }
        
        manager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            43200,
            service
        )

        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent = Intent(context, MainActivity::class.java)
                .let {
                        intent ->  PendingIntent.getActivity(context, 0, intent, 0)
                }

            val views = RemoteViews(
                context.packageName,
                R.layout.widget_clock_layout
            ).apply {
                setOnClickPendingIntent(R.id.widget_text, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("logo", "Received intent ${intent.action}")
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> Log.d("logo", "Screen turned on")
            Intent.ACTION_USER_BACKGROUND -> Log.d("logo", "User gone background")
            Intent.ACTION_USER_PRESENT -> Log.d("logo", "User is present")
        }
    }
}


