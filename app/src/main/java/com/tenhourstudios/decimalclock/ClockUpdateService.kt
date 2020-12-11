package com.tenhourstudios.decimalclock

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.icu.util.TimeZone
import android.os.Build
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
    private val CHANNEL_ID = "WidgetUpdateChannel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Entering onStartCommand")

        createNotificationChannel()

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.ticker_text))
            .build()

        // Notification ID cannot be 0.
        val ONGOING_NOTIFICATION_ID = 1
        startForeground(ONGOING_NOTIFICATION_ID, notification)

        val view = RemoteViews(packageName, R.layout.widget_clock_layout)
        val time = updateTime()
        view.setTextViewText(R.id.widget_text, time)
        view.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, 96F)

        val clockWidget = ComponentName(this, ClockAppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)

        manager.updateAppWidget(clockWidget, view)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "Entering onDestroy")
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        // check if OS version >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the Notification channel
            val name = getText(R.string.notification_channel_name)
            val descriptionText = getText(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText as String?
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun updateTime(): String {
        val millisToday = (System.currentTimeMillis() + timeZoneOffset) % MILLIS_IN_A_DAY
        val time = Clock(millisToday)
        val widgetText = time.tenHourTime(false, ":")

        Log.d(TAG, widgetText)
        return widgetText
    }
}
