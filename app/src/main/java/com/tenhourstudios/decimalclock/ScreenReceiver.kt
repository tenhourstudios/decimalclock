package com.tenhourstudios.decimalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> Log.d("pppp", "Screen turns on")
            Intent.ACTION_SCREEN_OFF -> Log.d("pppp", "Screen turns off")
            Intent.ACTION_USER_BACKGROUND -> Log.d("pppp", "User gone to background")
            Intent.ACTION_USER_PRESENT -> Log.d("pppp", "User present")
        }
    }
}