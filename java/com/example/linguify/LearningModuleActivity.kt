package com.example.linguify

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class LearningModuleActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_module)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        // Set up ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the header view and the profile photo ImageView
        val headerView: View = navView.getHeaderView(0)
        val profileImageView: ImageView = headerView.findViewById(R.id.imgProfilePhoto)

        // Make the profile photo clickable and lead to ProfileManagementActivity
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileManagementActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Set Navigation Item Click Listener
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_learn -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_practice -> {
                    startActivity(Intent(this, PracticeModuleActivity::class.java))
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

        // CardView for Phrase Library
        findViewById<CardView>(R.id.cardPhraseLibrary).setOnClickListener {
            startActivity(Intent(this, PhraseLibraryActivity::class.java))
        }

        // CardView for Favorites
        findViewById<CardView>(R.id.cardFavorites).setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle navigation menu toggle when the home button is pressed
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
