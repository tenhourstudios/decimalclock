package com.tenhourstudios.decimalclock.screens.settings

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tenhourstudios.decimalclock.R
import com.tenhourstudios.decimalclock.data.ClockAppWidgetProvider
import com.tenhourstudios.decimalclock.screens.about.AboutFragmentDirections
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val addWidgetPreference: Preference? = findPreference("add_widget")
        val appWidgetManager: AppWidgetManager = requireContext().getSystemService(AppWidgetManager::class.java)
        if (!appWidgetManager.isRequestPinAppWidgetSupported){
            addWidgetPreference?.isVisible = false
        }
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
        } else if (preference?.key == "add_widget") {
            addWidgetShortcut()
            return true
        }
        return false
    }

    private fun addWidgetShortcut() {
        val appWidgetManager: AppWidgetManager =
            requireContext().getSystemService(AppWidgetManager::class.java)
        val myProvider = ComponentName(requireContext(), ClockAppWidgetProvider::class.java)

        appWidgetManager.requestPinAppWidget(myProvider, null, null)
    }
}