package com.example.linguify

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun createLoginSession(email: String, password: String) {
        editor.putBoolean("isLoggedIn", true)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }

    fun getEmail(): String? {
        return prefs.getString("email", null)
    }

    fun getPassword(): String? {
        return prefs.getString("password", null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }

    fun saveUserProfile(firstName: String, lastName: String, contact: String, dob: String, profileImage: String) {
        editor.putString("first_name", firstName)
        editor.putString("last_name", lastName)
        editor.putString("contact", contact)
        editor.putString("dob", dob)
        editor.putString("profile_image", profileImage)
        editor.apply()
    }

    fun getUserProfile(): Map<String, String?> {
        return mapOf(
            "first_name" to prefs.getString("first_name", ""),
            "last_name" to prefs.getString("last_name", ""),
            "contact" to prefs.getString("contact", ""),
            "dob" to prefs.getString("dob", ""),
            "profile_image" to prefs.getString("profile_image", "")
        )
    }
}
