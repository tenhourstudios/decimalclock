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
    private var displayLabels: Boolean = false
    private var displaySeconds: Boolean = false
    var blinkingSeparator = false
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
        displayLabels = sharedPrefs.getBoolean("display_labels_preference", true)
        if (displayLabels) {
            binding.tenHourLabel.visibility = View.VISIBLE
            binding.twentyFourHourLabel.visibility = View.VISIBLE
        } else {
            binding.tenHourLabel.visibility = View.INVISIBLE
            binding.twentyFourHourLabel.visibility = View.INVISIBLE
        }
        displaySeconds = sharedPrefs.getBoolean("display_seconds_preference", false)
        blinkingSeparator = sharedPrefs.getBoolean("blinking_separator_preference", false)
        updateFrequency = when (blinkingSeparator || displaySeconds) {
            true -> 16
            false -> 1000
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
            val tClock = TenHourClock(millisToday)
            Timber.i("${tClock.getHourPadded()}:${tClock.getMinutePadded()}:${tClock.getSecondPadded()}")
            tenSeparator = when (blinkingSeparator && (tClock.getSecond() % 2 == 0)) {
                true -> " "
                false -> separator
            }
            binding.tenHourTime.text = time.tenHourTime(displaySeconds, tenSeparator)
            twentyFourSeparator = when (blinkingSeparator && (time.twentyFourSecond % 2 == 0)) {
                true -> " "
                false -> separator
            }
            binding.twentyFourHourTime.text =
                time.twentyFourHourTime(displaySeconds, twentyFourSeparator)

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