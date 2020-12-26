package com.tenhourstudios.decimalclock

import android.app.Application
import timber.log.Timber

class DecimalClockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}