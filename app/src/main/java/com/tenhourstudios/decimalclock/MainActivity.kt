package com.tenhourstudios.decimalclock

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val handler = Handler()
    val updateFrequency: Long = 10
    private var showDividers: Boolean = false

    private val updateTime = object: Runnable {
        override fun run() {
            val millisSinceEpoch = System.currentTimeMillis()
            val millisToday = millisSinceEpoch % 86400000
            tenHourTime.text = millisToTenHourTime(millisToday)
            twentyFourHourTime.text = millisToTwentyFourHourTime(millisToday)
            handler.postDelayed(this, updateFrequency)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("Note to self", Context.MODE_PRIVATE)
        showDividers = prefs.getBoolean("dividers", true)
        if (showDividers) {
            tenHourLabel.visibility = View.VISIBLE
        } else {
            tenHourLabel.visibility = View.INVISIBLE
        }
        startRepeatingTask()
    }

    override fun onStop() {
        super.onStop()
        stopRepeatingTask()
    }

    override fun onDestroy() {
        super.onDestroy()
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

    private fun millisToTenHourTime(millis: Long) : String {
        val seconds = (millis / 864) % 100
        val minutes = (millis / 86400) % 100
        val hours = (millis / 8640000)
        return "${hours.toString().padStart(2, '0')} : ${minutes.toString().padStart(2, '0')} : ${seconds.toString().padStart(2, '0')}"
    }

    private fun millisToTwentyFourHourTime(millis: Long) : String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 60000) % 60
        val hours = millis / 3600000
        return "${hours.toString().padStart(2, '0')} : ${minutes.toString().padStart(2, '0')} : ${seconds.toString().padStart(2, '0')}"
    }
}
