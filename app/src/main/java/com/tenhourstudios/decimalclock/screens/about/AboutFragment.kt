package com.tenhourstudios.decimalclock.screens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tenhourstudios.decimalclock.R
import com.tenhourstudios.decimalclock.databinding.FragmentAboutBinding

/**
 * Shows app version, description, and support links
 */
class AboutFragment : Fragment() {

    private lateinit var viewModel: AboutViewModel
    private lateinit var binding: FragmentAboutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)

        binding.aboutViewModel = viewModel
        binding.lifecycleOwner = this

        binding.aboutTwitterLink.setOnClickListener {
            viewModel.twitterLink.observe(viewLifecycleOwner, { link -> openUrl(getString(link)) })
        }
        binding.aboutEmailId.setOnClickListener {
            viewModel.emailId.observe(viewLifecycleOwner, { link -> openUrl(getString(link)) })
        }

        return binding.root
    }


    /**
     * Opens the Url
     */
    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}