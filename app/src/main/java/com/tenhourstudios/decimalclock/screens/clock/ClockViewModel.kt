package com.tenhourstudios.decimalclock.screens.clock

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tenhourstudios.decimalclock.data.KEY_PRECISION_LOW
import com.tenhourstudios.decimalclock.data.KEY_STANDARD
import com.tenhourstudios.decimalclock.data.MILLIS_IN_A_DAY
import com.tenhourstudios.decimalclock.data.clock.Clock
import java.time.Instant
import java.time.OffsetTime

class ClockViewModel : ViewModel() {

    // preferences initialization
    var displaySeconds: Boolean = false
    var blinkingSeparator = false
    var precision = KEY_PRECISION_LOW
    var format: String = KEY_STANDARD
    var updateFrequency: Long = 2

    // get the devices timezone
    private val timeZoneOffset = OffsetTime.now().offset

    private val _clock = MutableLiveData<Clock>()
    val clock: LiveData<Clock>
        get() = _clock

    val tenHourTime: LiveData<String> =  Transformations.map(clock) {
        currentClock ->
        currentClock.tenHourTime(format, precision)
    }

    val twentyFourHourTime: LiveData<String> =  Transformations.map(clock) {
            currentClock ->
        currentClock.twentyFourHourTime(precision)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val updateTime = object : Runnable {
        override fun run() {
            // get time in milliseconds since Unix epoch
            val millisSinceEpoch = Instant.now().toEpochMilli()

            // add offset and mod to get milliseconds since last midnight
            val millisToday =
                (millisSinceEpoch + 1000 * timeZoneOffset.totalSeconds) % MILLIS_IN_A_DAY

            _clock.value = Clock(millisToday)

            handler.postDelayed(this, updateFrequency)
        }
    }

    init {
        updateTime.run()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(updateTime)
    }
}