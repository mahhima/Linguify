package com.example.linguify

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TranslationPracticeActivity : AppCompatActivity() {

    private lateinit var tvTranslationPrompt: TextView
    private lateinit var tvPhraseToTranslate: TextView
    private lateinit var etTranslationInput: EditText
    private lateinit var btnSubmitTranslation: Button
    private lateinit var progressBar: ProgressBar

    // Translation maps for French and German
    private val frenchTranslations = mapOf(
        "Bonjour" to "Hello",
        "Merci" to "Thank you",
        "Au revoir" to "Goodbye",
        "S'il vous plaît" to "Please",
        "Comment ça va?" to "How are you?"
    )

    private val germanTranslations = mapOf(
        "Hallo" to "Hello",
        "Danke" to "Thank you",
        "Auf Wiedersehen" to "Goodbye",
        "Bitte" to "Please",
        "Wie geht's?" to "How are you?"
    )

    private var currentPhrase: String? = null
    private val usedPhrases = mutableSetOf<String>() // To track used phrases
    private var currentPhraseIndex = 0 // Track the current phrase index
    private var selectedLanguage: String = "French" // Default language

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation_practice)

        // Retrieve the selected language from Intent
        selectedLanguage = intent.getStringExtra("selectedLanguage") ?: "French"

        tvTranslationPrompt = findViewById(R.id.tvTranslationPrompt)
        tvPhraseToTranslate = findViewById(R.id.tvPhraseToTranslate)
        etTranslationInput = findViewById(R.id.etTranslationInput)
        btnSubmitTranslation = findViewById(R.id.btnSubmitTranslation)
        progressBar = findViewById(R.id.progressBar)

        // Initialize progress to 0
        progressBar.progress = 0

        loadNewPhrase()

        btnSubmitTranslation.setOnClickListener {
            checkTranslation()
        }
    }

    private fun loadNewPhrase() {
        // Select translations based on the chosen language
        val translations = if (selectedLanguage == "German") {
            germanTranslations
        } else {
            frenchTranslations
        }

        if (usedPhrases.size < translations.size) { // Check if there are unused phrases
            do {
                currentPhrase = translations.keys.random() // Get a random phrase
            } while (usedPhrases.contains(currentPhrase)) // Ensure it's not already used

            usedPhrases.add(currentPhrase!!) // Mark the phrase as used
            tvPhraseToTranslate.text = currentPhrase // Display the phrase to translate
            etTranslationInput.text.clear() // Clear the input field
        } else {
            showFinishDialog() // Show finish dialog if all phrases are used
        }
    }

    private fun checkTranslation() {
        // Select translations based on the chosen language
        val translations = if (selectedLanguage == "German") {
            germanTranslations
        } else {
            frenchTranslations
        }

        val userTranslation = etTranslationInput.text.toString().trim() // Trim leading and trailing whitespaces
        val correctTranslation = translations[currentPhrase]?.trim() // Also trim correct translation

        // Check if the user's translation matches the correct translation, ignoring case
        val message = if (userTranslation.equals(correctTranslation, ignoreCase = true)) {
            "Correct!"
        } else {
            "Incorrect! Correct answer: $correctTranslation"
        }

        AlertDialog.Builder(this)
            .setTitle("Result")
            .setMessage(message)
            .setPositiveButton("Next") { dialog, _ ->
                currentPhraseIndex++ // Increment the phrase index
                updateProgressBar() // Update progress bar after checking translation
                loadNewPhrase() // Load a new phrase after showing the result
                dialog.dismiss()
            }
            .show()
    }

    private fun updateProgressBar() {
        // Update progress bar based on the number of completed translations
        val translationsSize = if (selectedLanguage == "German") germanTranslations.size else frenchTranslations.size
        val progressPercentage = (currentPhraseIndex * 100) / translationsSize
        progressBar.progress = progressPercentage
    }

    private fun showFinishDialog() {
        AlertDialog.Builder(this)
            .setTitle("Practice Finished")
            .setMessage("You've completed all the translations!")
            .setPositiveButton("OK") { dialog, _ ->
                finish() // Close the activity or navigate to another screen
                dialog.dismiss()
            }
            .show()
    }
}
