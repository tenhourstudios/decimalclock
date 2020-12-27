package com.tenhourstudios.decimalclock

const val MILLIS_IN_A_DAY = 86400000

class TenHourClock(millis: Long) {

    // total milliseconds elapsed today
    val dMillis = millis * 100000000 / MILLIS_IN_A_DAY

    // total seconds elapsed today rounded down
    val dSecond = dMillis / 1000

    // total minutes elapsed today rounded down
    private val dMinute = dSecond / 100

    // total hours elapsed today rounded down
    private val dHour = dMinute / 100

    // returns seconds between 0 and 99 inclusive
    fun getSecond(): Int {
        return (dSecond % 100).toInt()
    }

    fun getSecondPadded(width: Int = 2): String {
        return this.getSecond().toString().padStart(width, '0')
    }

    // returns minutes between 0 and 99 inclusive
    fun getMinute(): Int {
        return (dMinute % 100).toInt()
    }

    fun getMinutePadded(width: Int = 2): String {
        return this.getMinute().toString().padStart(width, '0')
    }

    // returns hours between 0 and 9 inclusive
    fun getHour(): Int {
        return (dHour % 10).toInt()
    }

    fun getHourPadded(width: Int = 1): String {
        return this.getHour().toString().padStart(width, '0')
    }
}