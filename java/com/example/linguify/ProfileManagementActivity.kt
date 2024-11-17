package com.example.linguify

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ProfileManagementActivity : AppCompatActivity() {

    private lateinit var imgProfilePhoto: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var btnSave: Button
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etContact: EditText
    private lateinit var etDOB: EditText

    private var imageUri: Uri? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_management)

        imgProfilePhoto = findViewById(R.id.ivProfilePhoto)
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto)
        btnSave = findViewById(R.id.btnSaveProfile)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etContact = findViewById(R.id.etContact)
        etDOB = findViewById(R.id.etDOB)

        sessionManager = SessionManager(this)
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        loadUserProfile()

        btnUploadPhoto.setOnClickListener {
            openImageChooser()
        }

        btnSave.setOnClickListener {
            saveProfile()
        }

        etDOB.setOnClickListener {
            showDatePickerDialog()
        }

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data!!.data
                imgProfilePhoto.setImageURI(imageUri)
            }
        }
    }

    private fun loadUserProfile() {
        val email = sessionManager.getEmail() ?: return // Safeguard

        etEmail.setText(email)
        etPassword.setText(sessionManager.getPassword())

        // Load user profile data using email as the key
        etFirstName.setText(sharedPreferences.getString("${email}_first_name", "") ?: ".")
        etLastName.setText(sharedPreferences.getString("${email}_last_name", "") ?: ".")
        etContact.setText(sharedPreferences.getString("${email}_contact", "") ?: ".")
        etDOB.setText(sharedPreferences.getString("${email}_dob", "") ?: ".")

        val profileImagePath = sharedPreferences.getString("${email}_profile_image", "")
        if (!profileImagePath.isNullOrEmpty()) {
            Glide.with(this)
                .load(profileImagePath)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgProfilePhoto)
        } else {
            imgProfilePhoto.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }




    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun saveProfile() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = sessionManager.getEmail() ?: return // Safeguard
        val password = sessionManager.getPassword() ?: return // Safeguard
        val contact = etContact.text.toString().trim()
        val dob = etDOB.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val profileImagePath = saveImageToInternalStorage(imageUri)

        // Save the user profile data with email as key
        with(sharedPreferences.edit()) {
            putString("${email}_first_name", firstName)
            putString("${email}_last_name", lastName)
            putString("${email}_contact", contact)
            putString("${email}_dob", dob)
            putString("${email}_profile_image", profileImagePath) // Ensure this is saved correctly
            apply()
        }

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }



    private fun saveImageToInternalStorage(imageUri: Uri?): String {
        imageUri ?: return ""

        val file = File(filesDir, "${sessionManager.getEmail()}_profile_image.jpg")

        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input?.copyTo(output)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            etDOB.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day).show()
    }
}
