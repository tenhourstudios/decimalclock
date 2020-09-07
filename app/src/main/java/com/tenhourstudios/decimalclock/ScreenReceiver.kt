package com.tenhourstudios.decimalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "ScreenReceiver"

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Action: ${intent.action}")
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> Log.d("logo", "Boot completed")
            Intent.ACTION_SCREEN_ON -> Log.d("logo", "Screen turns on")
            Intent.ACTION_SCREEN_OFF -> Log.d("logo", "Screen turns off")
            Intent.ACTION_USER_BACKGROUND -> Log.d("logo", "User gone to background")
            Intent.ACTION_USER_PRESENT -> Log.d("logo", "User present")
        }
    }
}