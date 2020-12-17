package com.tenhourstudios.decimalclock

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar


class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val versionText = findViewById<TextView>(R.id.aboutAppVersion)
        versionText.text = String.format(
            getString(R.string.about_version),
            BuildConfig.VERSION_NAME
        )

        val twitterLink = findViewById<TextView>(R.id.aboutTwitterLink)
        val emailId = findViewById<TextView>(R.id.aboutEmailId)
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