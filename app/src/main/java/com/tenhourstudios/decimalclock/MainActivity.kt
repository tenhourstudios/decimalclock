package com.tenhourstudios.decimalclock

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import android.icu.util.*
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*

const val MILLIS_IN_A_DAY = 86400000

class MainActivity : AppCompatActivity() {

    val separator = " : "
    var tenSeparator = separator
    var twentyFourSeparator = separator

    // preferences initialization
    private var displayLabels: Boolean = false
    private var displaySeconds: Boolean = false
    var blinkingSeparator = false
    var updateFrequency: Long = 1000

    // get the devices timezone
    private var timeZone = TimeZone.getDefault() ?: TimeZone.GMT_ZONE
    private val timeZoneOffset = timeZone.rawOffset + timeZone.dstSavings

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateTheme()
    }

    override fun onResume() {
        super.onResume()

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        displayLabels = sharedPrefs.getBoolean("display_labels_preference", true)
        if (displayLabels) {
            tenHourLabel.visibility = View.VISIBLE
            twentyFourHourLabel.visibility = View.VISIBLE
        } else {
            tenHourLabel.visibility = View.INVISIBLE
            twentyFourHourLabel.visibility = View.INVISIBLE
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startUpdatingTime(){
        updateTime.run()
    }

    private fun stopUpdatingTime() {
        handler.removeCallbacks(updateTime)
    }

    private fun updateTheme() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val nightMode = when (sharedPrefs.getString("theme_preference", "System default"))
        {
            "Light" -> MODE_NIGHT_NO
            "Dark" -> MODE_NIGHT_YES
            else -> MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private val updateTime = object: Runnable {
        override fun run() {
            // get time in milliseconds since Unix epoch
            val millisSinceEpoch = System.currentTimeMillis()

            // add offset and mod to get milliseconds since last midnight
            val millisToday = (millisSinceEpoch + timeZoneOffset) % MILLIS_IN_A_DAY

            val time = Clock(millisToday)
            tenSeparator =  when (blinkingSeparator && (time.tenSecond % 2 == 0))  {
                true -> "   "
                false -> separator
            }
            tenHourTime.text = time.tenHourTime(displaySeconds, tenSeparator)
            twentyFourSeparator =  when (blinkingSeparator  && (time.twentyFourSecond % 2 == 0)) {
                true -> "   "
                false -> separator
            }
            twentyFourHourTime.text = time.twentyFourHourTime(displaySeconds, twentyFourSeparator)

            handler.postDelayed(this, updateFrequency)
        }
    }
}
