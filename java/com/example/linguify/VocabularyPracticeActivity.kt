package com.example.linguify

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VocabularyPracticeActivity : AppCompatActivity() {

    private lateinit var tvWordPrompt: TextView
    private lateinit var tvTranslationPrompt: TextView
    private lateinit var etTranslationInput: EditText
    private lateinit var btnSubmitTranslation: Button
    private lateinit var progressBar: ProgressBar

    private var currentQuestionIndex = 0
    private var selectedLanguage: String = "French" // Default language

    // Vocabulary maps for English to French and English to German
    private val frenchVocabulary = mapOf(
        "Hello" to "Bonjour",
        "Thank you" to "Merci",
        "Please" to "S'il vous plaît",
        "Goodbye" to "Au revoir",
        "Yes" to "Oui",
        "No" to "Non",
        "Help" to "Aidez-moi",
        "Water" to "Eau",
        "Food" to "Nourriture",
        "Friend" to "Ami(e)"
    )

    private val germanVocabulary = mapOf(
        "Hello" to "Hallo",
        "Thank you" to "Danke",
        "Please" to "Bitte",
        "Goodbye" to "Auf Wiedersehen",
        "Yes" to "Ja",
        "No" to "Nein",
        "Help" to "Hilfe",
        "Water" to "Wasser",
        "Food" to "Essen",
        "Friend" to "Freund(in)"
    )

    private var currentWord: String? = null
    private val usedWords = mutableSetOf<String>() // To track used words

    // Declare a variable to hold the current vocabulary map
    private lateinit var vocabulary: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocabulary_practice)

        // Retrieve the selected language from Intent
        selectedLanguage = intent.getStringExtra("selectedLanguage") ?: "French"

        tvWordPrompt = findViewById(R.id.tvWordPrompt)
        tvTranslationPrompt = findViewById(R.id.tvTranslationPrompt)
        etTranslationInput = findViewById(R.id.etTranslationInput)
        btnSubmitTranslation = findViewById(R.id.btnSubmitTranslation)
        progressBar = findViewById(R.id.progressBar)

        // Initialize progress to 0
        progressBar.progress = 0

        loadNewWord()

        btnSubmitTranslation.setOnClickListener {
            checkTranslation()
        }
    }

    private fun loadNewWord() {
        // Select vocabulary based on the chosen language
        vocabulary = if (selectedLanguage == "German") {
            tvTranslationPrompt.text = "Translate the following phrase to German: "
            germanVocabulary
        } else {
            tvTranslationPrompt.text = "Translate the following phrase to French: "
            frenchVocabulary
        }

        if (usedWords.size < vocabulary.size) { // Check if there are unused words
            do {
                currentWord = vocabulary.keys.random() // Get a random word
            } while (usedWords.contains(currentWord)) // Ensure it's not already used

            usedWords.add(currentWord!!) // Mark the word as used
            tvWordPrompt.text = currentWord // Display the word to translate
            etTranslationInput.text.clear() // Clear the input field
        } else {
            showFinishDialog() // Show finish dialog if all words are used
        }
    }

    private fun checkTranslation() {
        val userTranslation = etTranslationInput.text.toString().trim() // Trim leading and trailing whitespaces
        val correctTranslation = vocabulary[currentWord]?.trim() // Also trim correct translation

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
                currentQuestionIndex++ // Move to the next question only after checking
                updateProgressBar() // Update the progress bar after a question is done
                loadNewWord() // Load a new word after showing the result
                dialog.dismiss()
            }
            .show()
    }

    private fun updateProgressBar() {
        // Update progress bar based on the number of completed questions
        val vocabularySize = vocabulary.size // Use the class-level vocabulary variable
        val progressPercentage = (currentQuestionIndex * 100) / vocabularySize
        progressBar.progress = progressPercentage
    }

    private fun showFinishDialog() {
        AlertDialog.Builder(this)
            .setTitle("Practice Finished")
            .setMessage("You've completed all the vocabulary!")
            .setPositiveButton("OK") { dialog, _ ->
                currentQuestionIndex = 0 // Reset question index
                finish() // Close the activity or navigate to another screen
                dialog.dismiss()
            }
            .show()
    }
}
