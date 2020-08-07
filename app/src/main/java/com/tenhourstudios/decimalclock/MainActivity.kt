package com.tenhourstudios.decimalclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

const val MILLIS_IN_A_DAY = 86400000

class MainActivity : AppCompatActivity() {
    private val handler = Handler()
    var updateFrequency: Long = 1000
    private var displayLabels: Boolean = false
    private var displaySeconds: Boolean = false
    val separator = " : "
    var tenSeparator = separator
    var twentyFourSeparator = separator
    var blinkingSeparator = false

    private val updateTime = object: Runnable {
        override fun run() {
            val millisSinceEpoch = System.currentTimeMillis()
            val millisToday = millisSinceEpoch % MILLIS_IN_A_DAY
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        startRepeatingTask()
    }

    override fun onStop() {
        super.onStop()
        stopRepeatingTask()
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

    private fun startRepeatingTask(){
        updateTime.run()
    }

    private fun stopRepeatingTask() {
        handler.removeCallbacks(updateTime)
    }
}
