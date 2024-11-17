package com.example.linguify

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ProfileDatabaseHelper : AppCompatActivity() {

    private lateinit var imgProfilePhoto: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var btnSave: Button
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etContact: EditText
    private lateinit var etDOB: EditText

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

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

        // Upload photo button click listener
        btnUploadPhoto.setOnClickListener {
            openImageChooser()
        }

        // Save button click listener
        btnSave.setOnClickListener {
            saveProfile()
        }

        // Date of Birth field click listener
        etDOB.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imgProfilePhoto.setImageURI(imageUri) // Set the selected image to ImageView
        }
    }

    private fun saveProfile() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val contact = etContact.text.toString()
        val dob = etDOB.text.toString()

        // Here you can add code to save the profile information and imageUri
        // For this example, we'll just show a toast message
        Toast.makeText(this, "Profile Saved: $firstName $lastName, $email, $contact, $dob", Toast.LENGTH_SHORT).show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val dob = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            etDOB.setText(dob) // Set the selected date to EditText
        }, year, month, day)

        datePickerDialog.show()
    }
}
