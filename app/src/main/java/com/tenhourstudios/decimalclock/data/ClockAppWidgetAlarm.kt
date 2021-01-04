package com.tenhourstudios.decimalclock.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import timber.log.Timber
import java.time.OffsetTime
import java.util.*

class ClockAppWidgetAlarm(private val context: Context) {
    private val ALARM_ID = 0
    private val INTERVAL_MILLIS = 86400

    @SuppressLint("ShortAlarm")
    fun startAlarm() {
        val calendar = Calendar.getInstance()

        val timeZoneOffset = OffsetTime.now().offset
        val millisToday = (System.currentTimeMillis() + 1000 * timeZoneOffset.totalSeconds) % MILLIS_IN_A_DAY
        val millisUntilNext = INTERVAL_MILLIS - (millisToday % INTERVAL_MILLIS)
        Timber.i("$millisUntilNext ${millisUntilNext.toInt()}")

        calendar.add(Calendar.MILLISECOND, millisUntilNext.toInt())

        Timber.i("Millis in day: $millisUntilNext")
        Timber.i("10 time: ${TenHourClock(millisUntilNext).getStandard(KEY_PRECISION_MEDIUM)}")
        Timber.i("24 time: ${TwentyFourHourClock(millisUntilNext).twentyFourHourTime(KEY_PRECISION_MEDIUM)}")

        val millis = calendar.timeInMillis % MILLIS_IN_A_DAY
        Timber.i("Millis in day: $millis")
        Timber.i("10 time: ${TenHourClock(millis).getStandard(KEY_PRECISION_MEDIUM)}")
        Timber.i("24 time: ${TwentyFourHourClock(millis).twentyFourHourTime(KEY_PRECISION_MEDIUM)}")

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