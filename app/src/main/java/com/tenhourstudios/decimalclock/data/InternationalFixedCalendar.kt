package com.tenhourstudios.decimalclock.data

class InternationalFixedCalendar(dayOfYear: Int, year: Int) {

    val isLeapYear = year % 4 == 0 &&
            if (year % 100 == 0) year % 400 == 0 else true
}