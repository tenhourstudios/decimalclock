package com.tenhourstudios.decimalclock.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import timber.log.Timber
import java.util.*

class ClockAppWidgetAlarm(private val context: Context) {
    private val ALARM_ID = 0
    private val INTERVAL_MILLIS = 10000

    @SuppressLint("ShortAlarm")
    fun startAlarm() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS)
        val alarmIntent = Intent(context, ClockAppWidgetProvider::class.java)
        alarmIntent.action = ClockAppWidgetProvider().ACTION_AUTO_UPDATE
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // RTC does not wake the device up
        alarmManager.setRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            INTERVAL_MILLIS.toLong(),
            pendingIntent
        )
        Timber.i("Alarm started")
    }

    fun stopAlarm() {
        val alarmIntent = Intent(ClockAppWidgetProvider().ACTION_AUTO_UPDATE)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Timber.i("Alarm stopped")
    }

}