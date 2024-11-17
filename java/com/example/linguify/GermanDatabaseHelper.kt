package com.example.linguify

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GermanDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "german_phrases.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PHRASES = "phrases"
        private const val TABLE_FAVORITES = "favorites"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PHRASE = "phrase"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_MEANING = "meaning"
        private const val COLUMN_EXAMPLE = "example"
        private const val COLUMN_CULTURAL_INSIGHT = "cultural_insight"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createPhrasesTable = ("CREATE TABLE $TABLE_PHRASES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_CATEGORY TEXT, "
                + "$COLUMN_PHRASE TEXT, "
                + "$COLUMN_MEANING TEXT, "
                + "$COLUMN_EXAMPLE TEXT, "
                + "$COLUMN_CULTURAL_INSIGHT TEXT)")
        db?.execSQL(createPhrasesTable)

        val createFavoritesTable = ("CREATE TABLE $TABLE_FAVORITES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_PHRASE TEXT)")
        db?.execSQL(createFavoritesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PHRASES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getPhrases(category: String): List<Phrase> {
        val phrasesList = mutableListOf<Phrase>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_PHRASES, arrayOf(COLUMN_PHRASE, COLUMN_MEANING, COLUMN_EXAMPLE, COLUMN_CULTURAL_INSIGHT), "$COLUMN_CATEGORY=?", arrayOf(category), null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val phraseText = cursor.getString(cursor.getColumnIndex(COLUMN_PHRASE))
                val meaning = cursor.getString(cursor.getColumnIndex(COLUMN_MEANING))
                val example = cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLE))
                val culturalInsight = cursor.getString(cursor.getColumnIndex(COLUMN_CULTURAL_INSIGHT))
                phrasesList.add(Phrase(phraseText, meaning, example, culturalInsight)) // Update Phrase object
            } while (cursor.moveToNext())
        }
        cursor.close()
        return phrasesList
    }

    fun addPhrase(category: String, phrase: String, meaning: String, example: String, culturalInsight: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY, category)
            put(COLUMN_PHRASE, phrase)
            put(COLUMN_MEANING, meaning)
            put(COLUMN_EXAMPLE, example)
            put(COLUMN_CULTURAL_INSIGHT, culturalInsight)
        }
        db.insert(TABLE_PHRASES, null, values)
        db.close()
    }

    fun addToFavorites(phrase: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHRASE, phrase)
        }
        db.insert(TABLE_FAVORITES, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getFavorites(): List<String> {
        val favoritesList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_FAVORITES, arrayOf(COLUMN_PHRASE), null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val phraseText = cursor.getString(cursor.getColumnIndex(COLUMN_PHRASE))
                favoritesList.add(phraseText)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return favoritesList
    }

    fun deletePhrase(category: String, phrase: String) {
        val db = this.writableDatabase
        db.delete(TABLE_PHRASES, "$COLUMN_CATEGORY=? AND $COLUMN_PHRASE=?", arrayOf(category, phrase))
        db.close()
    }

    fun updatePhrase(category: String, oldPhrase: String, newPhrase: String, meaning: String, example: String, culturalInsight: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHRASE, newPhrase)
            put(COLUMN_MEANING, meaning)
            put(COLUMN_EXAMPLE, example)
            put(COLUMN_CULTURAL_INSIGHT, culturalInsight)
        }
        db.update(TABLE_PHRASES, values, "$COLUMN_CATEGORY=? AND $COLUMN_PHRASE=?", arrayOf(category, oldPhrase))
        db.close()
    }
}
