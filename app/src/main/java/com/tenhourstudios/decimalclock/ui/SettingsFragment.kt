package com.tenhourstudios.decimalclock.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tenhourstudios.decimalclock.R
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "theme_preference") {
            val nightMode = when (sharedPreferences.getString(
                key,
                getString(R.string.prefs_theme_system_default)
            )) {
                getString(R.string.prefs_theme_light) -> AppCompatDelegate.MODE_NIGHT_NO
                getString(R.string.prefs_theme_dark) -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            Timber.i("System theme changed to $nightMode")
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference?.key == "open_about") {
            this.findNavController().navigate(AboutFragmentDirections.actionGlobalAboutFragment())
            return true
        }
        return false
    }
}