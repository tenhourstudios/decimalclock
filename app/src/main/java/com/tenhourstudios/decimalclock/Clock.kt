package com.tenhourstudios.decimalclock

class Clock(millis: Long) {

    val dClock = TenHourClock(millis)
    fun tenHourTime(displaySeconds: Boolean, separator: String): String {
        var tenHourTime = "${dClock.getHourPadded()}$separator${dClock.getMinutePadded()}"
        if (displaySeconds) {
            tenHourTime += "$separator${dClock.getSecondPadded()}"
        }
        return tenHourTime
    }

    val twentyFourSecond = ((millis / 1000) % 60).toInt()
    val twentyFourMinute = (millis / (60 * 1000)) % 60
    val twentyFourHour = (millis / (60 * 60 * 1000))

    fun twentyFourHourTime(displaySeconds: Boolean, separator: String): String {
        var twentyFourHourTime = "${twentyFourHour.toString().padStart(2, '0')}$separator${
            twentyFourMinute.toString().padStart(2, '0')
        }"
        if (displaySeconds) {
            twentyFourHourTime += "$separator${twentyFourSecond.toString().padStart(2, '0')}"
        }
        return twentyFourHourTime
    }

}