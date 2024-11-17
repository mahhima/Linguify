package com.example.linguify

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

abstract class BaseDatabaseHelper(
    context: Context,
    private val databaseName: String,
    private val tableName: String
) : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1

        const val COLUMN_ID = "_id"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_PHRASE = "phrase"
        const val COLUMN_LANGUAGE = "language"  // New column for the Favorites table
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the main table for phrases
        val createTable = """
            CREATE TABLE $tableName (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_PHRASE TEXT
            )
        """
        db?.execSQL(createTable)

        // Create the Favorites table
        val createFavoritesTable = """
            CREATE TABLE FavoritesTable (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHRASE TEXT,
                $COLUMN_LANGUAGE TEXT
            )
        """
        db?.execSQL(createFavoritesTable)

        // Insert initial data into the language-specific phrases table
        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        db?.execSQL("DROP TABLE IF EXISTS FavoritesTable")
        onCreate(db)
    }

    protected abstract fun insertInitialData(db: SQLiteDatabase?)

    fun getPhrases(category: String): List<String> {
        val phrases = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.query(
            tableName,
            arrayOf(COLUMN_PHRASE),
            "$COLUMN_CATEGORY = ?",
            arrayOf(category),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                phrases.add(it.getString(it.getColumnIndexOrThrow(COLUMN_PHRASE)))
            }
        }
        return phrases
    }

    fun addPhrase(category: String, phrase: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY, category)
            put(COLUMN_PHRASE, phrase)
        }
        db.insert(tableName, null, values)
    }

    fun updatePhrase(category: String, oldPhrase: String, newPhrase: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHRASE, newPhrase)
        }
        db.update(tableName, values, "$COLUMN_CATEGORY = ? AND $COLUMN_PHRASE = ?", arrayOf(category, oldPhrase))
    }

    fun deletePhrase(category: String, phrase: String) {
        val db = writableDatabase
        db.delete(tableName, "$COLUMN_CATEGORY = ? AND $COLUMN_PHRASE = ?", arrayOf(category, phrase))
    }
}
