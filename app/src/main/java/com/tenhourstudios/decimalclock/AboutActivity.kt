package com.tenhourstudios.decimalclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
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
        versionText.text = String.format(getString(R.string.version_name), BuildConfig.VERSION_NAME)
        val twitterLink = findViewById<TextView>(R.id.aboutTwitterLink)
        val emailId = findViewById<TextView>(R.id.aboutEmailId)
        val supportKofi = findViewById<TextView>(R.id.aboutKofi)
        twitterLink.movementMethod = LinkMovementMethod.getInstance()
        emailId.movementMethod = LinkMovementMethod.getInstance()
        supportKofi.movementMethod = LinkMovementMethod.getInstance()
    }
}