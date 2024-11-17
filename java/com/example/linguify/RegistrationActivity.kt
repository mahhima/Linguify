package com.example.linguify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var sessionManager: SessionManager
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.password_repeat_input)
        registerButton = findViewById(R.id.register_button)
        loginLink = findViewById(R.id.login_link)

        sessionManager = SessionManager(this)
        databaseHelper = DatabaseHelper(this)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Check if the user already exists
                if (databaseHelper.checkUser(email)) {
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Add the new user to the database
                    databaseHelper.addUser(email, password)
                    sessionManager.createLoginSession(email, password)
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Navigate directly to DashboardActivity
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
