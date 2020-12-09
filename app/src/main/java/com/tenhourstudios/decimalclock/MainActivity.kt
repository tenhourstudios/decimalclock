package com.tenhourstudios.decimalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.util.TimeZone
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*

const val MILLIS_IN_A_DAY = 86400000

class MainActivity : AppCompatActivity() {

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
    private val timeZoneOffset = timeZone.rawOffset + timeZone.dstSavings

    private val handler = Handler()

    private val TAG = "ScreenBroadCastReceiver"

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                Log.d(TAG, "Action: ${intent.action}")
            }
            when (intent?.action) {
                Intent.ACTION_BOOT_COMPLETED -> Log.d(TAG, "Boot completed")
                Intent.ACTION_SCREEN_ON -> Log.d(TAG, "Screen turns on")
                Intent.ACTION_SCREEN_OFF -> Log.d(TAG, "Screen turns off")
                Intent.ACTION_USER_BACKGROUND -> Log.d(TAG, "User gone to background")
                Intent.ACTION_USER_PRESENT -> Log.d(TAG, "User present")
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_USER_BACKGROUND)
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        registerReceiver(broadcastReceiver, intentFilter)

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

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
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
        setDefaultNightMode(nightMode)

        val font = when (sharedPrefs.getString("font_preference", "Regular"))
        {
            "Thin" -> R.style.TimeFontThin
            "Casual" -> R.style.TimeFontCasual
            "Cursive" -> R.style.TimeFontCursive
            else -> R.style.TimeFontRegular
        }
        tenHourTime.setTextAppearance(font)
        twentyFourHourTime.setTextAppearance(font)

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
            tenHourTime.text = time.tenHourTime(displaySeconds, tenSeparator)
            twentyFourSeparator =  when (blinkingSeparator  && (time.twentyFourSecond % 2 == 0)) {
                true -> " "
                false -> separator
            }
            twentyFourHourTime.text = time.twentyFourHourTime(displaySeconds, twentyFourSeparator)

            handler.postDelayed(this, updateFrequency)
        }
    }
}
