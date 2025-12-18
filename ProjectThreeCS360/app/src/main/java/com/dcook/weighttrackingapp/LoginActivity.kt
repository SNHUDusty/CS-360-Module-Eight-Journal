package com.dcook.weighttrackingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    private lateinit var db: AppDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = AppDatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCreate = findViewById<Button>(R.id.btnCreateAccount)

        // LOGIN EXISTING USER
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = db.validateUser(username, password)
            if (userId != null) {
                Session.userId = userId
                Session.username = username
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid login. Try Create Account.", Toast.LENGTH_SHORT).show()
            }
        }

        // CREATE NEW ACCOUNT
        btnCreate.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val created = db.createUserIfNotExists(username, password)
            if (created != -1L) {
                val userId = db.getUserId(username) ?: -1
                Session.userId = userId
                Session.username = username
                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "User already exists. Use Login.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}