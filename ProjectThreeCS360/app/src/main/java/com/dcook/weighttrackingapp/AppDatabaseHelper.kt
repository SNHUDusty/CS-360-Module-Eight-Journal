package com.dcook.weighttrackingapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "weight_tracking.db"
        private const val DATABASE_VERSION = 1

        // Tables
        private const val TABLE_USERS = "users"
        private const val TABLE_WEIGHTS = "daily_weights"
        private const val TABLE_GOAL = "goal_weight"

        // Users columns
        private const val COL_USER_ID = "id"
        private const val COL_USERNAME = "username"
        private const val COL_PASSWORD = "password"

        // Weights columns
        private const val COL_WEIGHT_ID = "id"
        private const val COL_WEIGHT_VALUE = "weight"
        private const val COL_WEIGHT_DATE = "date"
        private const val COL_WEIGHT_USER_ID = "user_id"

        // Goal columns
        private const val COL_GOAL_ID = "id"
        private const val COL_GOAL_VALUE = "goal"
        private const val COL_GOAL_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Users table
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERNAME TEXT UNIQUE NOT NULL,
                $COL_PASSWORD TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Daily weights table
        db.execSQL(
            """
            CREATE TABLE $TABLE_WEIGHTS (
                $COL_WEIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_WEIGHT_VALUE REAL NOT NULL,
                $COL_WEIGHT_DATE TEXT NOT NULL,
                $COL_WEIGHT_USER_ID INTEGER NOT NULL,
                FOREIGN KEY($COL_WEIGHT_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID)
            )
            """.trimIndent()
        )

        // Goal weight table (one goal per user)
        db.execSQL(
            """
            CREATE TABLE $TABLE_GOAL (
                $COL_GOAL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_GOAL_VALUE REAL NOT NULL,
                $COL_GOAL_USER_ID INTEGER UNIQUE NOT NULL,
                FOREIGN KEY($COL_GOAL_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WEIGHTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // ---------------- USER (Create/Login) ----------------
    fun createUserIfNotExists(username: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_PASSWORD, password)
        }
        // Returns -1 if username already exists (unique constraint)
        return db.insert(TABLE_USERS, null, values)
    }

    fun validateUser(username: String, password: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_USER_ID FROM $TABLE_USERS WHERE $COL_USERNAME=? AND $COL_PASSWORD=?",
            arrayOf(username, password)
        )
        cursor.use {
            return if (it.moveToFirst()) it.getInt(0) else null
        }
    }

    fun getUserId(username: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_USER_ID FROM $TABLE_USERS WHERE $COL_USERNAME=?",
            arrayOf(username)
        )
        cursor.use {
            return if (it.moveToFirst()) it.getInt(0) else null
        }
    }

    // ---------------- GOAL (Create/Update/Read) ----------------
    fun setGoalForUser(userId: Int, goal: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_GOAL_VALUE, goal)
            put(COL_GOAL_USER_ID, userId)
        }

        // Try update first
        val updated = db.update(TABLE_GOAL, values, "$COL_GOAL_USER_ID=?", arrayOf(userId.toString()))
        if (updated > 0) return true

        // Insert if no goal exists yet
        return db.insert(TABLE_GOAL, null, values) != -1L
    }

    // ✅ Wrapper so your SetGoalActivity can call db.setGoal(...)
    fun setGoal(userId: Int, goal: Double): Boolean {
        return setGoalForUser(userId, goal)
    }

    fun getGoalForUser(userId: Int): Double? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_GOAL_VALUE FROM $TABLE_GOAL WHERE $COL_GOAL_USER_ID=?",
            arrayOf(userId.toString())
        )
        cursor.use {
            return if (it.moveToFirst()) it.getDouble(0) else null
        }
    }

    // ---------------- WEIGHTS (CRUD) ----------------
    data class WeightEntry(val id: Int, val weight: Double, val date: String)

    fun addWeight(userId: Int, weight: Double, date: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_WEIGHT_VALUE, weight)
            put(COL_WEIGHT_DATE, date)
            put(COL_WEIGHT_USER_ID, userId)
        }
        return db.insert(TABLE_WEIGHTS, null, values)
    }

    fun getAllWeights(userId: Int): List<WeightEntry> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_WEIGHT_ID, $COL_WEIGHT_VALUE, $COL_WEIGHT_DATE FROM $TABLE_WEIGHTS WHERE $COL_WEIGHT_USER_ID=? ORDER BY $COL_WEIGHT_ID DESC",
            arrayOf(userId.toString())
        )

        val list = mutableListOf<WeightEntry>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(WeightEntry(it.getInt(0), it.getDouble(1), it.getString(2)))
            }
        }
        return list
    }

    fun deleteWeight(id: Int): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_WEIGHTS, "$COL_WEIGHT_ID=?", arrayOf(id.toString())) > 0
    }

    fun updateWeight(id: Int, newWeight: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_WEIGHT_VALUE, newWeight)
        }
        return db.update(TABLE_WEIGHTS, values, "$COL_WEIGHT_ID=?", arrayOf(id.toString())) > 0
    }
}