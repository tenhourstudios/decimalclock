package com.tenhourstudios.decimalclock.data


class Clock(millis: Long, blinkingSeparator: Boolean) {

    private val dClock = TenHourClock(millis)
    private val tfClock = TwentyFourHourClock(millis)

    init{
        dClock.blinkingSeparator = blinkingSeparator
        tfClock.blinkingSeparator = blinkingSeparator
    }

    fun tenHourTime(format: String, precision: Int): String {
        return when (format) {
            KEY_DECIMAL -> dClock.getDecimal(precision)
            KEY_PERCENTAGE -> dClock.getPercentage(precision)
            else -> dClock.getStandard(precision)
        }
    }

    fun twentyFourHourTime(precision: Int): String {
        return tfClock.twentyFourHourTime(precision)
    }

}