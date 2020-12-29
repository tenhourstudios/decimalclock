package com.tenhourstudios.decimalclock.data.clock

import com.tenhourstudios.decimalclock.data.KEY_DECIMAL
import com.tenhourstudios.decimalclock.data.KEY_PERCENTAGE
import com.tenhourstudios.decimalclock.data.KEY_PRECISION_HIGH
import com.tenhourstudios.decimalclock.data.KEY_PRECISION_MEDIUM


class Clock(private val millis: Long) {

    private val dClock = TenHourClock(millis)

    fun tenHourTime(format: String, precision: Int): String {
        return when (format) {
            KEY_DECIMAL -> dClock.getDecimal(precision)
            KEY_PERCENTAGE -> dClock.getPercentage(precision)
            else -> dClock.getStandard(precision)
        }
    }

    val twentyFourSecond = ((millis / 1000) % 60).toInt()
    val twentyFourMinute = (millis / (60 * 1000)) % 60
    val twentyFourHour = (millis / (60 * 60 * 1000))

    fun twentyFourHourTime(precision: Int): String {
        var twentyFourHourTime = "${twentyFourHour.toString().padStart(2, '0')}:${
            twentyFourMinute.toString().padStart(2, '0')
        }"
        if (precision >= KEY_PRECISION_MEDIUM) {
            twentyFourHourTime += ":${twentyFourSecond.toString().padStart(2, '0')}"
        }
        if (precision >= KEY_PRECISION_HIGH) {
            twentyFourHourTime += ":" + "${millis /10 % 100}".padStart(2, '0')
        }
        return twentyFourHourTime
    }

}