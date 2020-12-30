package com.tenhourstudios.decimalclock.screens.clock

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.PreferenceManager
import com.tenhourstudios.decimalclock.R
import com.tenhourstudios.decimalclock.data.*
import com.tenhourstudios.decimalclock.databinding.FragmentClockBinding


class ClockFragment : Fragment() {

    private lateinit var viewModel: ClockViewModel
    private lateinit var binding: FragmentClockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_clock, container, false)
        viewModel = ViewModelProvider(this).get(ClockViewModel::class.java)


        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity)
        //viewModel.blinkingSeparator = sharedPrefs.getBoolean("blinking_separator_preference", false)
        viewModel.format = when (sharedPrefs.getString(
            "preference_format_digital",
            getString(R.string.prefs_digital_standard)
        )) {
            getString(R.string.prefs_digital_decimal) -> KEY_DECIMAL
            getString(R.string.prefs_digital_percentage) -> KEY_PERCENTAGE
            else -> KEY_STANDARD
        }
        viewModel.precision = when (sharedPrefs.getString(
            "preference_precision_digital",
            getString(R.string.prefs_precision_low)
        )) {
            getString(R.string.prefs_precision_medium) -> KEY_PRECISION_MEDIUM
            getString(R.string.prefs_precision_high) -> KEY_PRECISION_HIGH
            else -> KEY_PRECISION_LOW
        }
        viewModel.updateFrequency = when (viewModel.precision) {
            KEY_PRECISION_MEDIUM -> 16
            KEY_PRECISION_HIGH -> 4
            else -> 1000
        }

        binding.clockViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(this.findNavController())
                || super.onOptionsItemSelected(item)
    }
}