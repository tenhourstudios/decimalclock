package com.tenhourstudios.decimalclock

import com.tenhourstudios.decimalclock.data.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun clock_isCorrect() {
        val tfMillis: Long = 3600000
        val tfTime = TwentyFourHourClock(tfMillis)
        assertEquals(tfTime.twentyFourHourTime(KEY_PRECISION_LOW), "01:00")
        assertEquals(tfTime.twentyFourHourTime(KEY_PRECISION_MEDIUM), "01:00:00")
        assertEquals(tfTime.twentyFourHourTime(KEY_PRECISION_HIGH), "01:00:00:00")

        val tMillis: Long = 8640000
        var tTime = TenHourClock(tMillis)
        assertEquals(tTime.getStandard(KEY_PRECISION_LOW), "1:00")
        assertEquals(tTime.getStandard(KEY_PRECISION_MEDIUM), "1:00:00")
        assertEquals(tTime.getStandard(KEY_PRECISION_HIGH), "1:00:00:00")

        assertEquals(tTime.getDecimal(KEY_PRECISION_LOW), "1.00")
        assertEquals(tTime.getDecimal(KEY_PRECISION_MEDIUM), "1.0000")
        assertEquals(tTime.getDecimal(KEY_PRECISION_HIGH), "1.000000")

        assertEquals(tTime.getPercentage(KEY_PRECISION_LOW), "10.0%")
        assertEquals(tTime.getPercentage(KEY_PRECISION_MEDIUM), "10.000%")
        assertEquals(tTime.getPercentage(KEY_PRECISION_HIGH), "10.00000%")

        val dMillis: Long = 51860000
        tTime = TenHourClock(dMillis)
        assertEquals(tTime.getStandard(KEY_PRECISION_LOW), "6:00")
        assertEquals(tTime.getStandard(KEY_PRECISION_MEDIUM), "6:00:23")
        assertEquals(tTime.getStandard(KEY_PRECISION_HIGH), "6:00:23:14")

        assertEquals(tTime.getDecimal(KEY_PRECISION_LOW), "6.00")
        assertEquals(tTime.getDecimal(KEY_PRECISION_MEDIUM), "6.0023")
        assertEquals(tTime.getDecimal(KEY_PRECISION_HIGH), "6.002314")

        assertEquals(tTime.getPercentage(KEY_PRECISION_LOW), "60.0%")
        assertEquals(tTime.getPercentage(KEY_PRECISION_MEDIUM), "60.023%")
        assertEquals(tTime.getPercentage(KEY_PRECISION_HIGH), "60.02314%")
    }
}
