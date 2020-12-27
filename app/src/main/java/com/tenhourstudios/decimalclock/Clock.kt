package com.tenhourstudios.decimalclock

const val KEY_STANDARD = "format_standard"
const val KEY_DECIMAL = "format_decimal"
const val KEY_PERCENTAGE = "format_percentage"
const val KEY_PRECISION_LOW = 3
const val KEY_PRECISION_MEDIUM = 5
const val KEY_PRECISION_HIGH = 7

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