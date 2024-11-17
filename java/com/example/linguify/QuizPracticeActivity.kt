package com.example.linguify

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizPracticeActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    private var selectedAnswer: String? = null // Variable to hold the selected answer
    private var currentQuestionIndex = 0

    private val frenchQuestions = listOf(
        QuizQuestion("What is the capital of France?", "Paris", "London", "Berlin", "Madrid", "Paris"),
        QuizQuestion("What is the French word for 'apple'?", "Pomme", "Orange", "Banane", "Raisin", "Pomme"),
        QuizQuestion("Which of the following is a French greeting?", "Goodbye", "Bonjour", "Thanks", "Yes", "Bonjour"),
        QuizQuestion("What does 'Merci' mean in English?", "Please", "Hello", "Thank you", "Goodbye", "Thank you"),
        QuizQuestion("Which verb means 'to be' in French?", "Avoir", "Être", "Aller", "Faire", "Être")
    )

    private val germanQuestions = listOf(
        QuizQuestion("What is the capital of Germany?", "Berlin", "Munich", "Frankfurt", "Hamburg", "Berlin"),
        QuizQuestion("What is the German word for 'apple'?", "Apfel", "Orange", "Banane", "Traube", "Apfel"),
        QuizQuestion("Which of the following is a German greeting?", "Tschüss", "Hallo", "Merci", "Yes", "Hallo"),
        QuizQuestion("What does 'Danke' mean in English?", "Please", "Goodbye", "Thank you", "Hello", "Thank you"),
        QuizQuestion("Which verb means 'to be' in German?", "Haben", "Sein", "Gehen", "Machen", "Sein")
    )

    private lateinit var questions: List<QuizQuestion> // To hold the current set of questions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_practice_activity)

        tvQuestion = findViewById(R.id.tvQuestion)
        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById(R.id.progressBar)

        // Get the selected language from the Intent
        val selectedLanguage = intent.getStringExtra("selectedLanguage")

        // Log the selected language for debugging
        Log.d("QuizPracticeActivity", "Selected Language: $selectedLanguage")

        // Load the questions based on the selected language
        questions = if (selectedLanguage == "German") {
            germanQuestions
        } else {
            frenchQuestions // Default to French if no or invalid language is passed
        }

        displayQuestion()

        btnSubmit.setOnClickListener {
            showResult()
        }

        // Disable Submit button initially
        btnSubmit.isEnabled = false

        // Set OnClickListener for answer buttons
        val answerButtons = listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4)
        answerButtons.forEach { button ->
            button.setOnClickListener {
                selectedAnswer = button.text.toString() // Store the selected answer
                btnSubmit.isEnabled = true

                // Reset the background for all buttons
                answerButtons.forEach {
                    it.setBackgroundResource(R.drawable.button_shape_default) // Reset to default background
                }

                // Set the selected background for the clicked button
                button.setBackgroundResource(R.drawable.button_shape_selected) // Set selected background
            }
        }
    }

    private fun displayQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        tvQuestion.text = currentQuestion.question
        btnAnswer1.text = currentQuestion.option1
        btnAnswer2.text = currentQuestion.option2
        btnAnswer3.text = currentQuestion.option3
        btnAnswer4.text = currentQuestion.option4

        // Reset selected answer
        selectedAnswer = null
        btnSubmit.isEnabled = false // Reset Submit button
        val answerButtons = listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4)
        answerButtons.forEach { it.isSelected = false } // Reset button states
    }

    private fun showResult() {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        val message = if (selectedAnswer == correctAnswer) {
            "Correct!"
        } else {
            "Wrong! Correct answer is: $correctAnswer"
        }

        AlertDialog.Builder(this)
            .setTitle("Result")
            .setMessage(message)
            .setPositiveButton("Next") { dialog, _ ->
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    displayQuestion()
                    updateProgressBar() // Update progress bar here
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Quiz Finished")
                        .setMessage("You've completed the quiz!")
                        .setPositiveButton("OK") { _, _ -> finish() }
                        .show()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun updateProgressBar() {
        // Update progress bar based on the number of completed questions
        val progressPercentage = ((currentQuestionIndex + 1) * 100) / questions.size
        progressBar.progress = progressPercentage
    }
}

data class QuizQuestion(
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctAnswer: String
)
