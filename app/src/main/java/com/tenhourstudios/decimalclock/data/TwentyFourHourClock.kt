package com.tenhourstudios.decimalclock.data

class TwentyFourHourClock(private val millis: Long) {

    var blinkingSeparator = false
    private val tfSecond = ((millis / 1000) % 60).toInt()
    private val twentyFourMinute = (millis / (60 * 1000)) % 60
    private val tfHour = (millis / (60 * 60 * 1000))

    private fun isEvenSecond(second: Int): Boolean {
        return second % 2 == 0
    }

    fun twentyFourHourTime(precision: Int): String {
        val separator = if (isEvenSecond(tfSecond) && blinkingSeparator) " " else  ":"
        var twentyFourHourTime = "${tfHour.toString().padStart(2, '0')}$separator${
            twentyFourMinute.toString().padStart(2, '0')
        }"
        if (precision >= KEY_PRECISION_MEDIUM) {
            twentyFourHourTime += "$separator${tfSecond.toString().padStart(2, '0')}"
        }
        if (precision >= KEY_PRECISION_HIGH) {
            twentyFourHourTime += "$separator${(millis / 10 % 100).toString().padStart(2, '0')}"
        }
        return twentyFourHourTime
    }

}