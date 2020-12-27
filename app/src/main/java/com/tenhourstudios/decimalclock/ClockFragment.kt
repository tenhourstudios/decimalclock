package com.tenhourstudios.decimalclock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.PreferenceManager
import com.tenhourstudios.decimalclock.databinding.FragmentClockBinding
import timber.log.Timber
import java.time.Instant
import java.time.OffsetTime


class ClockFragment : Fragment() {
    private var _binding: FragmentClockBinding? = null

    private val binding get() = _binding!!

    private val separator = ":"
    var tenSeparator = separator
    var twentyFourSeparator = separator

    // preferences initialization
    private var displaySeconds: Boolean = false
    var blinkingSeparator = false
    var precision = KEY_PRECISION_LOW
    var format: String = KEY_STANDARD
    var updateFrequency: Long = 1000

    // get the devices timezone
    private val timeZoneOffset = OffsetTime.now().offset


    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Timber.d(timeZoneOffset.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentClockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity)
        displaySeconds = sharedPrefs.getBoolean("display_seconds_preference", false)
        blinkingSeparator = sharedPrefs.getBoolean("blinking_separator_preference", false)
        format = when(sharedPrefs.getString("preference_format_digital", getString(R.string.prefs_digital_standard)))
        {
            getString(R.string.prefs_digital_decimal) -> KEY_DECIMAL
            getString(R.string.prefs_digital_percentage) -> KEY_PERCENTAGE
            else -> KEY_STANDARD
        }
        precision = when(sharedPrefs.getString("preference_precision_digital", getString(R.string.prefs_precision_low)))
        {
            getString(R.string.prefs_precision_medium) -> KEY_PRECISION_MEDIUM
            getString(R.string.prefs_precision_high) -> KEY_PRECISION_HIGH
            else -> KEY_PRECISION_LOW
        }
        updateFrequency = when (precision) {
            KEY_PRECISION_MEDIUM -> 16
            KEY_PRECISION_HIGH -> 4
            else -> 1000
        }
        startUpdatingTime()
    }

    private val updateTime = object : Runnable {

        override fun run() {
            // get time in milliseconds since Unix epoch
            val millisSinceEpoch = Instant.now().toEpochMilli()

            // add offset and mod to get milliseconds since last midnight
            val millisToday =
                (millisSinceEpoch + 1000 * timeZoneOffset.totalSeconds) % MILLIS_IN_A_DAY

            val time = Clock(millisToday)
            binding.tenHourTime.text = time.tenHourTime(format, precision)
            /*
            val tClock = TenHourClock(millisToday)
            tenSeparator = when (blinkingSeparator && (tClock.getSecond() % 2 == 0)) {
                true -> " "
                false -> separator
            }
            twentyFourSeparator = when (blinkingSeparator && (time.twentyFourSecond % 2 == 0)) {
                true -> " "
                false -> separator
            }

 */
            binding.twentyFourHourTime.text =
                time.twentyFourHourTime(precision)

            handler.postDelayed(this, updateFrequency)
        }
    }

    override fun onStop() {
        stopUpdatingTime()
        super.onStop()
    }

    private fun startUpdatingTime() {
        updateTime.run()
    }

    private fun stopUpdatingTime() {
        handler.removeCallbacks(updateTime)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(this.findNavController())
                || super.onOptionsItemSelected(item)
    }
}