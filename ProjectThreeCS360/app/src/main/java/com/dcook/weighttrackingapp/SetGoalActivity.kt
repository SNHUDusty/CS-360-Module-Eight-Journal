package com.dcook.weighttrackingapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class SetGoalActivity : ComponentActivity() {

    private lateinit var db: AppDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goal)

        db = AppDatabaseHelper(this)

        val etGoal = findViewById<EditText>(R.id.etGoalWeight)
        val btnSave = findViewById<Button>(R.id.btnSaveGoal)
        val btnCancel = findViewById<Button>(R.id.btnCancelGoal)

        btnSave.setOnClickListener {
            val raw = etGoal.text.toString().trim()

            if (Session.userId == -1) {
                Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (raw.isEmpty()) {
                etGoal.error = "Enter a goal weight"
                return@setOnClickListener
            }

            val goal = raw.toDoubleOrNull()
            if (goal == null || goal <= 0) {
                etGoal.error = "Enter a valid number"
                return@setOnClickListener
            }

            val ok = db.setGoal(Session.userId, goal)
            if (ok) {
                Toast.makeText(this, "Goal saved.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Save failed.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}