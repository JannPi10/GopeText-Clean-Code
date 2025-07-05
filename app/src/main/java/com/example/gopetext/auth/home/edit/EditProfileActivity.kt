package com.example.gopetext.auth.home.edit

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.auth.home.fragments.profile.ProfileContract
import com.example.gopetext.auth.home.fragments.profile.ProfilePresenter
import com.example.gopetext.data.model.User
import com.example.gopetext.data.storage.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditProfileActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter
    private lateinit var sessionManager: SessionManager

    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var ivProfileImage: ImageView
    private lateinit var etAge: EditText
    private lateinit var btnSave: Button
    private lateinit var btnChangePhoto: Button

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).into(ivProfileImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sessionManager = SessionManager(this)
        presenter = ProfilePresenter(this, sessionManager)

        etName = findViewById(R.id.edt_Name)
        etLastName = findViewById(R.id.edt_LastName)
        etAge = findViewById(R.id.edt_Age)
        ivProfileImage = findViewById(R.id.img_Profile)
        btnSave = findViewById(R.id.btn_Save)
        btnChangePhoto = findViewById(R.id.btn_ChangePhoto)

        btnChangePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val age = etAge.text.toString().trim()

            if (name.isEmpty() || lastName.isEmpty() ||age.isEmpty()) {
                Toast.makeText(this, "Nombre y apellido son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var photoPart: MultipartBody.Part? = null

            selectedImageUri?.let { uri ->
                val mimeType = contentResolver.getType(uri)
                if (mimeType != "image/jpeg" && mimeType != "image/png") {
                    showError("Solo se permiten imágenes JPG o PNG")
                    return@setOnClickListener
                }

                val fileBytes = contentResolver.openInputStream(uri)?.readBytes()
                val requestBody = fileBytes?.toRequestBody(mimeType.toMediaTypeOrNull())
                requestBody?.let {
                    photoPart = MultipartBody.Part.createFormData("nombreimage", "profile.jpg", it)
                }
            }

            //presenter.updateUser(name, lastName, photoPart)
        }

        presenter.loadUserData()
    }

    override fun showUserData(user: User) {
        etName.setText(user.name)
        etLastName.setText(user.last_name)
        etAge.setText(user.age.toString())
        Glide.with(this).load(user.profile_image_url).into(ivProfileImage)
    }

    override fun showUpdateSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish() // Cierra y vuelve al fragmento
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
