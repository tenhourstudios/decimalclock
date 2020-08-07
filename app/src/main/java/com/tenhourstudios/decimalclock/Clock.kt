package com.tenhourstudios.decimalclock

class Clock(millis: Long) {
    val tenSecond = ((millis / 864) % 100).toInt()
    val tenMinute = (millis / 86400) % 100
    val tenHour = (millis / 8640000)

    fun tenHourTime(displaySeconds: Boolean, separator: String) : String {
        var tenHourTime = "${tenHour.toString().padStart(2, '0')}$separator${tenMinute.toString().padStart(2, '0')}"
        if (displaySeconds) {
            tenHourTime += "$separator${tenSecond.toString().padStart(2, '0')}"
        }
        return tenHourTime
    }
    val twentyFourSecond = ((millis / 1000) % 60).toInt()
    val twentyFourMinute = (millis / (60 * 1000)) % 60
    val twentyFourHour = (millis / (60 * 60 * 1000))

    fun twentyFourHourTime(displaySeconds: Boolean, separator: String) : String {
        var twentyFourHourTime = "${twentyFourHour.toString().padStart(2, '0')}$separator${twentyFourMinute.toString().padStart(2, '0')}"
        if (displaySeconds) {
            twentyFourHourTime += "$separator${twentyFourSecond.toString().padStart(2, '0')}"
        }
        return twentyFourHourTime
    }

}