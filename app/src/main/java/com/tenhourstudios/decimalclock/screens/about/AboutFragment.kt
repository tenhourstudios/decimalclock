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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val twitterLink = binding.aboutTwitterLink
        val emailId = binding.aboutEmailId
        twitterLink.setOnClickListener {
            openUrl(getString(R.string.twitter_link))
        }
        emailId.setOnClickListener {
            openUrl(getString(R.string.email_id))
        }
    }

    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}