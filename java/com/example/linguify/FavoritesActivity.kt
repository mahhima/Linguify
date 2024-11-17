package com.example.linguify

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var listViewFavorites: ListView
    private lateinit var languageSpinner: Spinner
    private lateinit var germanDbHelper: GermanDatabaseHelper
    private lateinit var frenchDbHelper: FrenchDatabaseHelper
    private var selectedLanguage: String = "German"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Initialize ListView and Spinner
        listViewFavorites = findViewById(R.id.listViewFavorites)
        languageSpinner = findViewById(R.id.languageSpinner)
        germanDbHelper = GermanDatabaseHelper(this)
        frenchDbHelper = FrenchDatabaseHelper(this)

        // Set up language spinner
        val languages = arrayOf("German", "French")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = spinnerAdapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLanguage = parent.getItemAtPosition(position).toString()
                loadFavorites() // Load favorites for the selected language
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set up item click listener for removing favorites
        listViewFavorites.setOnItemClickListener { parent, view, position, id ->
            val selectedPhrase = parent.getItemAtPosition(position).toString()
            removeFromFavorites(selectedPhrase) // Remove selected phrase from favorites
            loadFavorites() // Refresh the favorites list
        }
    }

    private fun loadFavorites() {
        val favorites = if (selectedLanguage == "German") {
            germanDbHelper.getFavorites()
        } else {
            frenchDbHelper.getFavorites()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favorites)
        listViewFavorites.adapter = adapter
    }

    private fun removeFromFavorites(phrase: String) {
        if (selectedLanguage == "German") {
            germanDbHelper.deletePhrase("Favorites", phrase)
        } else {
            frenchDbHelper.deletePhrase("Favorites", phrase)
        }
    }
}
