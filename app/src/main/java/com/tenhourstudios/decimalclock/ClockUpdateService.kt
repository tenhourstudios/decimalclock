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
        return null
    }

    private var timeZone = TimeZone.getDefault() ?: TimeZone.GMT_ZONE
    private val timeZoneOffset = timeZone.rawOffset + timeZone.dstSavings

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val millisToday = (System.currentTimeMillis() + timeZoneOffset) % MILLIS_IN_A_DAY
        val time = Clock(millisToday)
        val widgetText = time.tenHourTime(false, ":")

        val TAG = "ClockUpdateService"
        Log.d(TAG, widgetText)

        val view = RemoteViews(packageName, R.layout.widget_clock_layout)
        view.setTextViewText(R.id.widget_text, widgetText)
        view.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, 96F)

        val clockWidget = ComponentName(this, ClockAppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)

        manager.updateAppWidget(clockWidget, view)

        return super.onStartCommand(intent, flags, startId)
    }
}
