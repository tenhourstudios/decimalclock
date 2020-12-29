package com.tenhourstudios.decimalclock.screens.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tenhourstudios.decimalclock.BuildConfig

class AboutViewModel : ViewModel() {
    private val _appVersionText = MutableLiveData<String>()
    val appVersionText: LiveData<String>
        get() = _appVersionText

    init {
        _appVersionText.value = BuildConfig.VERSION_NAME
    }


}