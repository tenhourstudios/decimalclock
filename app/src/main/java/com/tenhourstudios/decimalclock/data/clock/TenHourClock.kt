package com.tenhourstudios.decimalclock.data.clock

import com.tenhourstudios.decimalclock.data.KEY_PRECISION_HIGH
import com.tenhourstudios.decimalclock.data.KEY_PRECISION_MEDIUM
import com.tenhourstudios.decimalclock.data.MILLIS_IN_A_DAY


class TenHourClock(millis: Long) {

    // total milliseconds elapsed today
    private val dMillis = millis * 100000000 / MILLIS_IN_A_DAY

    // total seconds elapsed today rounded down
    private val dSecond = dMillis / 1000

    // total minutes elapsed today rounded down
    private val dMinute = dSecond / 100

    // total hours elapsed today rounded down
    private val dHour = dMinute / 100

    private fun getMillisecondPadded(width: Int = 2): String {
        return (dMillis/10 % 100).toString().padStart(width, '0')
    }

    // returns seconds between 0 and 99 inclusive
    private fun getSecond(): Int {
        return (dSecond % 100).toInt()
    }

    private fun getSecondPadded(width: Int = 2): String {
        return this.getSecond().toString().padStart(width, '0')
    }

    // returns minutes between 0 and 99 inclusive
    private fun getMinute(): Int {
        return (dMinute % 100).toInt()
    }

    private fun getMinutePadded(width: Int = 2): String {
        return this.getMinute().toString().padStart(width, '0')
    }

    // returns hours between 0 and 9 inclusive
    private fun getHour(): Int {
        return (dHour % 10).toInt()
    }

    private fun getHourPadded(width: Int = 1): String {
        return this.getHour().toString().padStart(width, '0')
    }

    fun  getStandard(precision: Int): String{
        var standardTime = "${this.getHourPadded()}:${this.getMinutePadded()}"
        if (precision >= KEY_PRECISION_MEDIUM) {
            standardTime += ":${this.getSecondPadded()}"
        }
        if (precision >= KEY_PRECISION_HIGH) {
            standardTime += ":${this.getMillisecondPadded()}"
        }
        return standardTime
    }
    fun getDecimal(precision: Int): String{
        return "${this.getHourPadded()}." + dMillis.toString().slice(1 until precision)
    }

    fun getPercentage(precision: Int): String{
        return "${dMinute/10}." + dMillis.toString().slice(2 until precision) + "%"
    }
}