package com.tenhourstudios.decimalclock.screens.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tenhourstudios.decimalclock.BuildConfig
import com.tenhourstudios.decimalclock.R

class AboutViewModel : ViewModel() {
    private val _appVersionText = MutableLiveData<String>()
    val appVersionText: LiveData<String>
        get() = _appVersionText

    private val _twitterLink = MutableLiveData<Int>()
    val twitterLink: LiveData<Int>
        get() = _twitterLink

    private val _emailId = MutableLiveData<Int>()
    val emailId: LiveData<Int>
        get() = _emailId

    init {
        _appVersionText.value = BuildConfig.VERSION_NAME
        _twitterLink.value = R.string.twitter_link
        _emailId.value = R.string.email_id
    }

}