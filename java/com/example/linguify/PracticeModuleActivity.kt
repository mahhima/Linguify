package com.example.linguify

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.cardview.widget.CardView
import com.google.android.material.navigation.NavigationView

class PracticeModuleActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var spinnerLanguages: Spinner
    private var selectedLanguage: String = "French" // Default language

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_module)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Retrieve the header view and the profile photo ImageView
        val headerView: View = navView.getHeaderView(0)
        val profileImageView: ImageView = headerView.findViewById(R.id.imgProfilePhoto)

        // Make the profile photo clickable and lead to ProfileManagementActivity
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileManagementActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_learn -> {
                    startActivity(Intent(this, LearningModuleActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_practice -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    // Create a confirmation dialog
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Confirm Logout")
                    builder.setMessage("Are you sure you want to logout?")

                    builder.setPositiveButton("Yes") { _, _ ->
                        // Clear sensitive user data from SharedPreferences
                        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.clear() // Clear all sensitive user data
                        editor.apply() // or editor.commit()

                        // Navigate to LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Finish the current activity (DashboardActivity)
                    }

                    builder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss() // Dismiss the dialog
                    }

                    // Show the dialog
                    val dialog = builder.create()
                    dialog.show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }

        // Initialize the language spinner
        spinnerLanguages = findViewById(R.id.spinnerLanguages)
        spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLanguage = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Default behavior, if nothing selected
            }
        }

        // CardView for Quiz Practice
        findViewById<CardView>(R.id.cardQuizPractice).setOnClickListener {
            val intent = Intent(this, QuizPracticeActivity::class.java)
            intent.putExtra("selectedLanguage", selectedLanguage) // Pass the selected language
            startActivity(intent)
        }

        // CardView for Translation Practice
        findViewById<CardView>(R.id.cardTranslationPractice).setOnClickListener {
            val intent = Intent(this, TranslationPracticeActivity::class.java)
            intent.putExtra("selectedLanguage", selectedLanguage) // Pass the selected language
            startActivity(intent)
        }

        // CardView for Vocabulary Practice
        findViewById<CardView>(R.id.cardVocabularyPractice).setOnClickListener {
            val intent = Intent(this, VocabularyPracticeActivity::class.java)
            intent.putExtra("selectedLanguage", selectedLanguage) // Pass the selected language
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
