package com.example.gopetext.auth.home.edit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.data.api.FileUtil
import com.example.gopetext.data.model.User
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class EditProfileActivity : AppCompatActivity(), EditProfileContract.View {

    private lateinit var presenter: EditProfileContract.Presenter
    private lateinit var sessionManager: SessionManager

    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etAge: EditText
    private lateinit var btnSave: Button
    private lateinit var ivProfileImage: ImageView
    private lateinit var btnSelectImage: Button

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                ivProfileImage.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sessionManager = SessionManager(this)
        presenter = EditProfilePresenter(this, sessionManager)

        etName = findViewById(R.id.edt_Name)
        etLastName = findViewById(R.id.edt_LastName)
        etAge = findViewById(R.id.edt_Age)
        btnSave = findViewById(R.id.btn_Save)
        ivProfileImage = findViewById(R.id.img_Profile)
        btnSelectImage = findViewById(R.id.btn_ChangePhoto)

        btnSelectImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val age = etAge.text.toString().trim().toIntOrNull() ?: 0

            val imagePart = selectedImageUri?.let { uri ->
                Log.d("EditProfile", "Imagen seleccionada URI: $uri")
                val file = FileUtil.from(this, uri)
                Log.d("EditProfile", "Imagen convertida a File: ${file.name}, size=${file.length()}")
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }

            presenter.updateUserProfile(name, lastName, age, imagePart)
        }

        presenter.loadUserProfile()
    }

    override fun showUserData(user: User) {
        etName.setText(user.name)
        etLastName.setText(user.last_name)
        etAge.setText(user.age.toString())

        val imageUrl = user.profile_image_url?.let {
            if (it.startsWith("http")) it else Constants.BASE_URL + it.removePrefix("/")
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(ivProfileImage)
        } else {
            ivProfileImage.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun goBackProfile() {
        finish()
    }
}



