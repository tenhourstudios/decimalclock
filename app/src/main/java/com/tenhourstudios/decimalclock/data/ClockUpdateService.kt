package com.tenhourstudios.decimalclock.data

import android.app.*
import android.app.Notification.CATEGORY_SERVICE
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.annotation.Nullable
import androidx.preference.PreferenceManager
import com.tenhourstudios.decimalclock.MainActivity
import com.tenhourstudios.decimalclock.R
import com.tenhourstudios.decimalclock.data.clock.Clock
import timber.log.Timber
import java.time.OffsetTime

class ClockUpdateService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("Entering onBind")
        return null
    }

    private val timeZoneOffset = OffsetTime.now().offset
    private val channelId = "WidgetUpdateChannel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Entering onStartCommand")

        createNotificationChannel()

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
            .setCategory(CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.notification_ticker_text))
            .build()

        // Notification ID cannot be 0.
        val ongoingNotificationId = 1
        startForeground(ongoingNotificationId, notification)

        val view = RemoteViews(packageName, R.layout.widget_clock_layout)
        val time = updateTime()
        view.setTextViewText(R.id.widget_text, time)
        view.setTextViewTextSize(R.id.widget_text, TypedValue.COMPLEX_UNIT_SP, 72F)

        val clockWidget = ComponentName(this, ClockAppWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)

        manager.updateAppWidget(clockWidget, view)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Timber.d("Entering onDestroy")
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        // Create the Notification channel
        val name = getText(R.string.notification_channel_name)
        val descriptionText = getText(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = descriptionText as String?
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    private fun updateTime(): String {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val format = when(sharedPrefs.getString("preference_format_digital", getString(R.string.prefs_digital_standard)))
        {
            getString(R.string.prefs_digital_decimal) -> KEY_DECIMAL
            getString(R.string.prefs_digital_percentage) -> KEY_PERCENTAGE
            else -> KEY_STANDARD
        }
        val millisToday = (System.currentTimeMillis() + 1000 * timeZoneOffset.totalSeconds) % MILLIS_IN_A_DAY
        val time = Clock(millisToday)
        val widgetText = time.tenHourTime(format, KEY_PRECISION_LOW)

        Timber.i(widgetText)
        return widgetText
    }
}
