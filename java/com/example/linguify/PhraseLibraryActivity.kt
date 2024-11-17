package com.example.linguify

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PhraseLibraryActivity : AppCompatActivity() {

    private lateinit var listViewPhrases: ListView
    private lateinit var languageSpinner: Spinner
    private lateinit var editTextPhrase: EditText
    private lateinit var editTextMeaning: EditText
    private lateinit var editTextExample: EditText
    private lateinit var editTextCulturalInsight: EditText
    private lateinit var buttonAddPhrase: Button
    private lateinit var germanDbHelper: GermanDatabaseHelper
    private lateinit var frenchDbHelper: FrenchDatabaseHelper
    private var selectedLanguage: String = "German"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phrase_library) // Ensure this matches your layout file

        // Initialize views
        listViewPhrases = findViewById(R.id.listViewPhrases)
        languageSpinner = findViewById(R.id.languageSpinner)
        editTextPhrase = findViewById(R.id.editTextPhrase)
        editTextMeaning = findViewById(R.id.editTextMeaning)
        editTextExample = findViewById(R.id.editTextExample)
        editTextCulturalInsight = findViewById(R.id.editTextCulturalInsight)
        buttonAddPhrase = findViewById(R.id.buttonAddPhrase)

        // Initialize database helpers
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
                loadPhrases("Greetings")  // Load default category after language selection
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set up button click listener for adding phrases
        buttonAddPhrase.setOnClickListener {
            val phrase = editTextPhrase.text.toString()
            val meaning = editTextMeaning.text.toString()
            val example = editTextExample.text.toString()
            val culturalInsight = editTextCulturalInsight.text.toString()

            if (selectedLanguage == "German") {
                germanDbHelper.addPhrase("Greetings", phrase, meaning, example, culturalInsight)
            } else {
                frenchDbHelper.addPhrase("Greetings", phrase, meaning, example, culturalInsight)
            }

            loadPhrases("Greetings") // Refresh the list
        }

        // Register ListView for context menu (if needed)
        registerForContextMenu(listViewPhrases)
    }

    private fun loadPhrases(category: String) {
        val phrases = if (selectedLanguage == "German") {
            germanDbHelper.getPhrases(category)
        } else {
            frenchDbHelper.getPhrases(category)
        }
        val adapter = PhraseAdapterLibrary(this, phrases)
        listViewPhrases.adapter = adapter
    }
}
