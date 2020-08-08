package com.tenhourstudios.decimalclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.icu.util.TimeZone
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.Nullable


class ClockUpdateService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var timeZone = TimeZone.getDefault() ?: TimeZone.GMT_ZONE
    private val timeZoneOffset = timeZone.rawOffset + timeZone.dstSavings

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val widgetText = ((System.currentTimeMillis() + timeZoneOffset) % MILLIS_IN_A_DAY) / 86400
        Log.d("logo", widgetText.toString())
        Log.d("logo", "Adding one to number")
        val view = RemoteViews(packageName, R.layout.widget_clock_layout)
        view.setTextViewText(R.id.widget_text, widgetText.toString())
        val theWidget = ComponentName(this, ClockAppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)
        manager.updateAppWidget(theWidget, view)
        return super.onStartCommand(intent, flags, startId)
    }
}
