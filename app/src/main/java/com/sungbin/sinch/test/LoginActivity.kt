package com.sungbin.sinch.test

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        if (ContextCompat.checkSelfPermission(
                this@LoginActivity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@LoginActivity,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_PHONE_STATE
                ),
                1
            )
        }

        findViewById<View>(R.id.loginButton).setOnClickListener {
            val intent = Intent(applicationContext, CallActivity::class.java)
            intent.putExtra(
                "callerId",
                (findViewById<View>(R.id.callerId) as TextView).text.toString()
            )
            intent.putExtra(
                "recipientId",
                (findViewById<View>(R.id.recipientId) as TextView).text.toString()
            )
            startActivity(intent)
        }
    }

}