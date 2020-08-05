package com.tenhourstudios.decimalclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var number = 10
    private val handler = Handler()
    val delay: Long = 100

    private val updateTime = object: Runnable {
        override fun run() {
            Log.d("Logo", "runnable runs")
            number = addOne(number)
            textView.text = number.toString()
            handler.postDelayed(this, delay)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.text = number.toString()
    }

    override fun onResume() {
        super.onResume()
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
                textView.text = "about clicked"
                true
            }
            R.id.settings ->  {
                textView.text = "settings clicked"
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun addOne(number: Int) : Int {
        return number + 1
    }

    private fun startRepeatingTask(){
        updateTime.run()
    }

    private fun stopRepeatingTask() {
        handler.removeCallbacks(updateTime)
    }

}
