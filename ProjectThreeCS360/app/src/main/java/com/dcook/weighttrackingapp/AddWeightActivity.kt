package com.dcook.weighttrackingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddWeightActivity : ComponentActivity() {

    private lateinit var db: AppDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_weight)

        db = AppDatabaseHelper(this)

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val btnSave = findViewById<Button>(R.id.btnSaveWeight)
        val btnCancel = findViewById<Button>(R.id.btnCancelWeight)

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        tvDate.text = "Date: $today"

        btnSave.setOnClickListener {
            val raw = etWeight.text.toString().trim()

            if (Session.userId == -1) {
                Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (raw.isEmpty()) {
                etWeight.error = "Enter a weight"
                return@setOnClickListener
            }

            val weight = raw.toDoubleOrNull()
            if (weight == null || weight <= 0) {
                etWeight.error = "Enter a valid number"
                return@setOnClickListener
            }

            // ✅ Save weight to SQLite
            val result = db.addWeight(Session.userId, weight, today)

            if (result != -1L) {
                // ✅ After saving, check goal and send SMS if goal reached
                val goal = db.getGoalForUser(Session.userId)
                if (goal != null && weight <= goal) {
                    sendGoalReachedSms(goal)
                }

                Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show()
                finish() // back to Dashboard (it refreshes in onResume)
            } else {
                Toast.makeText(this, "Save failed.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    // ✅ Sends SMS ONLY if permission granted. If denied, app continues normally.
    private fun sendGoalReachedSms(goal: Double) {
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) return

        try {
            val smsManager = SmsManager.getDefault()

            // Emulator-friendly test number (you can change later if needed)
            val phoneNumber = "5551234567"

            val message = "Congratulations! You reached your goal weight of $goal lbs!"

            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}