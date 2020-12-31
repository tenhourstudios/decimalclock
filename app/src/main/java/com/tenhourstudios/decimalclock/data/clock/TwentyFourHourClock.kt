package com.tenhourstudios.decimalclock.data.clock

import com.tenhourstudios.decimalclock.data.KEY_PRECISION_HIGH
import com.tenhourstudios.decimalclock.data.KEY_PRECISION_MEDIUM

class TwentyFourHourClock(private val millis: Long) {

    private val twentyFourSecond = ((millis / 1000) % 60).toInt()
    private val twentyFourMinute = (millis / (60 * 1000)) % 60
    private val twentyFourHour = (millis / (60 * 60 * 1000))

    fun twentyFourHourTime(precision: Int): String {
        var twentyFourHourTime = "${twentyFourHour.toString().padStart(2, '0')}:${
            twentyFourMinute.toString().padStart(2, '0')
        }"
        if (precision >= KEY_PRECISION_MEDIUM) {
            twentyFourHourTime += ":${twentyFourSecond.toString().padStart(2, '0')}"
        }
        if (precision >= KEY_PRECISION_HIGH) {
            twentyFourHourTime += ":" + "${millis / 10 % 100}".padStart(2, '0')
        }
        return twentyFourHourTime
    }

}