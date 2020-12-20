package com.tenhourstudios.decimalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.util.TimeZone
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.tenhourstudios.decimalclock.databinding.ActivityMainBinding

const val MILLIS_IN_A_DAY = 86400000

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val separator = ":"
    var tenSeparator = separator
    var twentyFourSeparator = separator

    // preferences initialization
    private var displayLabels: Boolean = false
    private var displaySeconds: Boolean = false
    var blinkingSeparator = false
    var updateFrequency: Long = 1000

    // get the devices timezone
    private var timeZone = TimeZone.getDefault() ?: TimeZone.GMT_ZONE
    private val timeZoneOffset = timeZone.rawOffset

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.about ->  {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings ->  {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateTheme()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
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

    override fun onStop() {
        super.onStop()
        stopUpdatingTime()
    }

    private fun startUpdatingTime(){
        updateTime.run()
    }

    private fun stopUpdatingTime() {
        handler.removeCallbacks(updateTime)
    }

    private fun updateTheme() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val nightMode = when (sharedPrefs.getString("theme_preference", getString(R.string.prefs_theme_system_default)))
        {
            getString(R.string.prefs_theme_light) -> MODE_NIGHT_NO
            getString(R.string.prefs_theme_dark) -> MODE_NIGHT_YES
            else -> MODE_NIGHT_FOLLOW_SYSTEM
        }
        setDefaultNightMode(nightMode)
    }

    private val updateTime = object: Runnable {
        override fun run() {
            // get time in milliseconds since Unix epoch
            val millisSinceEpoch = System.currentTimeMillis()

            // add offset and mod to get milliseconds since last midnight
            val millisToday = (millisSinceEpoch + timeZoneOffset) % MILLIS_IN_A_DAY

            val time = Clock(millisToday)
            tenSeparator =  when (blinkingSeparator && (time.tenSecond % 2 == 0))  {
                true -> " "
                false -> separator
            }
            binding.tenHourTime.text = time.tenHourTime(displaySeconds, tenSeparator)
            twentyFourSeparator =  when (blinkingSeparator  && (time.twentyFourSecond % 2 == 0)) {
                true -> " "
                false -> separator
            }
            binding.twentyFourHourTime.text = time.twentyFourHourTime(displaySeconds, twentyFourSeparator)

            handler.postDelayed(this, updateFrequency)
        }
    }
}
