package com.dcook.weighttrackingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity : ComponentActivity() {

    private lateinit var db: AppDatabaseHelper
    private lateinit var rv: RecyclerView
    private lateinit var adapter: WeightAdapter
    private lateinit var tvGoal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db = AppDatabaseHelper(this)

        tvGoal = findViewById(R.id.tvCurrentGoal)
        rv = findViewById(R.id.rvWeights)

        adapter = WeightAdapter(mutableListOf()) { entry ->
            val deleted = db.deleteWeight(entry.id)
            if (deleted) {
                Toast.makeText(this, "Deleted entry.", Toast.LENGTH_SHORT).show()
                refreshList()
            }
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<Button>(R.id.btnAddWeight).setOnClickListener {
            startActivity(Intent(this, AddWeightActivity::class.java))
        }

        findViewById<Button>(R.id.btnSetGoal).setOnClickListener {
            startActivity(Intent(this, SetGoalActivity::class.java))
        }

        // ✅ NEW: Open SMS Permission screen
        findViewById<Button>(R.id.btnSmsSettings).setOnClickListener {
            startActivity(Intent(this, SmsPermissionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
        refreshGoal()
    }

    private fun refreshList() {
        if (Session.userId == -1) return
        val items = db.getAllWeights(Session.userId)
        adapter.updateData(items)
    }

    private fun refreshGoal() {
        if (Session.userId == -1) return
        val goal = db.getGoalForUser(Session.userId)
        tvGoal.text = if (goal == null) "Goal: -- lbs" else "Goal: $goal lbs"
    }
}