package com.dcook.weighttrackingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class SmsPermissionActivity : ComponentActivity() {

    private lateinit var tvStatus: TextView

    private val requestSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Toast.makeText(
                this,
                if (granted) "SMS permission granted." else "SMS permission denied.",
                Toast.LENGTH_SHORT
            ).show()
            updateStatus()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_permission)

        tvStatus = findViewById(R.id.tvSmsStatus)
        val btnRequest = findViewById<Button>(R.id.btnRequestSmsPermission)

        updateStatus()

        btnRequest.setOnClickListener {
            val alreadyGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED

            if (alreadyGranted) {
                Toast.makeText(this, "SMS permission already granted.", Toast.LENGTH_SHORT).show()
                updateStatus()
            } else {
                requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }
    }

    private fun updateStatus() {
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED

        tvStatus.text = if (granted) {
            "SMS notifications are enabled."
        } else {
            "SMS notifications are disabled."
        }
    }
}