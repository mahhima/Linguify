package com.example.linguify

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2 // Increment this if you change the schema
        private const val DATABASE_NAME = "linguify.db"
        private const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_CONTACT = "contact"
        const val COLUMN_DOB = "dob"
        const val COLUMN_PROFILE_IMAGE = "profile_image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_EMAIL TEXT UNIQUE NOT NULL," +
                "$COLUMN_PASSWORD TEXT NOT NULL," +
                "$COLUMN_FIRST_NAME TEXT DEFAULT 'John'," +
                "$COLUMN_LAST_NAME TEXT DEFAULT 'Doe'," +
                "$COLUMN_CONTACT TEXT DEFAULT '0000000000'," +
                "$COLUMN_DOB TEXT DEFAULT '01/01/2000'," +
                "$COLUMN_PROFILE_IMAGE TEXT DEFAULT 'default_image.png')")

        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        // Create a new table
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This is not typical to implement for production, but included if needed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password)
        )
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun addUser(email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?", arrayOf(email))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun getUserByEmail(email: String): Cursor? {
        val db = this.readableDatabase
        return db.query(TABLE_USERS, null, "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)
    }

    fun updateUserProfile(email: String, firstName: String, lastName: String, contact: String, dob: String, profileImage: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_CONTACT, contact)
            put(COLUMN_DOB, dob)
            put(COLUMN_PROFILE_IMAGE, profileImage)
        }
        return db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
    }
}
