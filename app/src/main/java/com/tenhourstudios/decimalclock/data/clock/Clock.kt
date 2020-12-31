package com.tenhourstudios.decimalclock.data.clock

import com.tenhourstudios.decimalclock.data.KEY_DECIMAL
import com.tenhourstudios.decimalclock.data.KEY_PERCENTAGE


class Clock(millis: Long) {

    private val dClock = TenHourClock(millis)

    fun tenHourTime(format: String, precision: Int): String {
        return when (format) {
            KEY_DECIMAL -> dClock.getDecimal(precision)
            KEY_PERCENTAGE -> dClock.getPercentage(precision)
            else -> dClock.getStandard(precision)
        }
    }

    private val tfClock = TwentyFourHourClock(millis)

    fun twentyFourHourTime(precision: Int): String {
        return tfClock.twentyFourHourTime(precision)
    }

}