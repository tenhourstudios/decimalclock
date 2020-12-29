package com.tenhourstudios.decimalclock.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tenhourstudios.decimalclock.BuildConfig
import com.tenhourstudios.decimalclock.R
import com.tenhourstudios.decimalclock.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val versionText = binding.aboutAppVersion
        versionText.text = String.format(
            getString(R.string.about_version),
            BuildConfig.VERSION_NAME
        )

        val twitterLink = binding.aboutTwitterLink
        val emailId = binding.aboutEmailId
        twitterLink.setOnClickListener {
            val url = getString(R.string.twitter_link)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        emailId.setOnClickListener {
            val url = getString(R.string.email_id)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}