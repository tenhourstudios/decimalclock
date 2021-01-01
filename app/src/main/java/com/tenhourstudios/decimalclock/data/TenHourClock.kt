package com.tenhourstudios.decimalclock.data

/**
 * This class computes the Ten Hour time given the milliseconds elapsed in the day.
 */
class TenHourClock(millis: Long) {

    var blinkingSeparator = false

    private val dMillis = (millis * 100000000 / MILLIS_IN_A_DAY).toInt()

    /** total seconds elapsed today rounded down */
    private val dSecond = dMillis / 1000

    /** total minutes elapsed today rounded down */
    private val dMinute = dSecond / 100

    /** total hours elapsed today rounded down */
    private val dHour = dMinute / 100

    private fun isEvenSecond(second: Int): Boolean {
        return second % 2 == 0
    }

    /**
     * Formats the decimal time in hours, minutes and seconds
     * @return a string of the form h:mm:ss:uu
     */
    fun getStandard(precision: Int): String {
        val separator = if (isEvenSecond(dSecond) && blinkingSeparator) " " else  ":"

        var standardTime = "$dHour$separator${this.getMinutePadded()}"
        if (precision >= KEY_PRECISION_MEDIUM) {
            standardTime += "$separator${this.getSecondPadded()}"
        }
        if (precision >= KEY_PRECISION_HIGH) {
            standardTime += "$separator${this.getCentisecondPadded()}"
        }
        return standardTime
    }

    /**
     * Formats the decimal time as a decimal between 0 and 10.
     * @return a string of the form h.mmssuu
     */
    fun getDecimal(precision: Int): String {
        val separator = if (isEvenSecond(dSecond) && blinkingSeparator) " " else  "."
        return "$dHour$separator" + this.getMillisecondPadded().slice(1 until precision)
    }

    fun getPercentage(precision: Int): String {
        val separator = if (isEvenSecond(dSecond) && blinkingSeparator) " " else  "."
        return "${dMinute / 10}$separator" + this.getMillisecondPadded()
            .slice(2 until precision) + "%"
    }


    private fun getMillisecondPadded(width: Int = 8): String {
        return dMillis.toString().padStart(width, '0')
    }

    private fun getCentisecondPadded(width: Int = 2): String {
        return ((dMillis / 10) % 100).toString().padStart(width, '0')
    }

    /**
     * Computes seconds elapsed in the current minute
     * @return an integer between 0 and 99 inclusive
     */
    private fun getSecond(): Int {
        return (dSecond % 100).toInt()
    }

    private fun getSecondPadded(width: Int = 2): String {
        return this.getSecond().toString().padStart(width, '0')
    }

    /**
     * Computes minutes elapsed in the current hour
     * @return an integer between 0 and 99 inclusive
     */
    private fun getMinute(): Int {
        return (dMinute % 100).toInt()
    }

    private fun getMinutePadded(width: Int = 2): String {
        return this.getMinute().toString().padStart(width, '0')
    }

    /**
     * Computes hours elapsed in the current day
     * @return an integer between 0 and 9 inclusive
     */
    private fun getHour(): Int {
        return (dHour % 10).toInt()
    }

    private fun getHourPadded(width: Int = 1): String {
        return this.getHour().toString().padStart(width, '0')
    }

}