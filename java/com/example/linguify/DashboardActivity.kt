package com.example.linguify

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView

class DashboardActivity : BaseActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sessionManager: SessionManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize SessionManager and SharedPreferences
        sessionManager = SessionManager(this)
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        val drawerLayout: DrawerLayout = getDrawerLayout()
        val navView: NavigationView = findViewById(R.id.navView)

        val headerView: View = navView.getHeaderView(0)
        val profileImageView: ImageView = headerView.findViewById(R.id.imgProfilePhoto)
        val usernameTextView: TextView = headerView.findViewById(R.id.tvUsername)

        // Load the profile image and username
        loadProfileImage(profileImageView)
        loadWelcomeMessage(usernameTextView)

        profileImageView.setOnClickListener {
            startActivity(Intent(this, ProfileManagementActivity::class.java))
        }

        // Set up navigation item selection listener
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_learn -> {
                    Log.d("DashboardActivity", "Learn menu item clicked")
                    startActivity(Intent(this, LearningModuleActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_practice -> {
                    Log.d("DashboardActivity", "Practice menu item clicked")
                    startActivity(Intent(this, PracticeModuleActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    showLogoutConfirmationDialog()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }

        // Set up drawer toggle
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up button click listeners
        setupButtonListeners()
    }

    // Reload the profile image and welcome message when the activity resumes
    override fun onResume() {
        super.onResume()
        val headerView: View = findViewById<NavigationView>(R.id.navView).getHeaderView(0)
        val profileImageView: ImageView = headerView.findViewById(R.id.imgProfilePhoto)
        val usernameTextView: TextView = headerView.findViewById(R.id.tvUsername)

        loadProfileImage(profileImageView)
        loadWelcomeMessage(usernameTextView)
    }

    private fun setupButtonListeners() {
        val learnButton: Button = findViewById(R.id.btnLearningModule)
        val practiceButton: Button = findViewById(R.id.btnPracticeModule)
        val logoutButton: Button = findViewById(R.id.btnLogout)

        learnButton.setOnClickListener {
            Log.d("DashboardActivity", "Learn button clicked")
            startActivity(Intent(this, LearningModuleActivity::class.java))
        }

        practiceButton.setOnClickListener {
            Log.d("DashboardActivity", "Practice button clicked")
            startActivity(Intent(this, PracticeModuleActivity::class.java))
        }

        logoutButton.setOnClickListener {
            Log.d("DashboardActivity", "Logout button clicked")
            showLogoutConfirmationDialog()
        }
    }

    // Load user profile image from SharedPreferences
    private fun loadProfileImage(profileImageView: ImageView) {
        val email = sessionManager.getEmail() ?: return
        val profileImagePath = sharedPreferences.getString("${email}_profile_image", "")

        if (!profileImagePath.isNullOrEmpty()) {
            Glide.with(this)
                .load(profileImagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Avoid using cached images
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(profileImageView)
        } else {
            profileImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }


    // Load the welcome message with the user's first name from SharedPreferences
    private fun loadWelcomeMessage(usernameTextView: TextView) {
        val email = sessionManager.getEmail() ?: return // Safeguard
        val firstName = sharedPreferences.getString("${email}_first_name", "")

        usernameTextView.text = if (!firstName.isNullOrEmpty()) {
            "Welcome, $firstName"
        } else {
            "Welcome"
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
            .setMessage("Do you want to log out?")
            .setPositiveButton("Yes") { dialog, which ->
                logout()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun logout() {
        // Clear only session-related data, not the user profile data
        sessionManager.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
