package com.tenhourstudios.decimalclock

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.icu.util.TimeZone
import android.os.IBinder
import android.util.Log
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.annotation.Nullable


class ClockUpdateService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "Entering onBind")
        return null
    }

    private val TAG = "ClockUpdateService"
    private var timeZone = TimeZone.getDefault() ?: TimeZone.GMT_ZONE
    private val timeZoneOffset = timeZone.rawOffset + timeZone.dstSavings

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Entering onStartCommand")
        val view = RemoteViews(packageName, R.layout.widget_clock_layout)
        val time = updateTime()
        view.setTextViewText(R.id.widget_text, time)
        view.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, 96F)

        val clockWidget = ComponentName(this, ClockAppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)

        manager.updateAppWidget(clockWidget, view)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateTime(): String {
        val millisToday = (System.currentTimeMillis() + timeZoneOffset) % MILLIS_IN_A_DAY
        val time = Clock(millisToday)
        val widgetText = time.tenHourTime(false, ":")

        Log.d(TAG, widgetText)
        return widgetText
    }
}
