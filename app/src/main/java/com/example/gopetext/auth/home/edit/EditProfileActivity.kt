package com.example.gopetext.auth.home.edit

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gopetext.data.model.User
import com.example.gopetext.data.repository.UserRepository
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.databinding.ActivityEditProfileBinding
import com.example.gopetext.utils.ErrorHandler
import com.example.gopetext.utils.ImageLoader
import com.example.gopetext.utils.validators.ProfileValidator
import com.example.gopetext.utils.factories.ImagePartFactory

class EditProfileActivity : AppCompatActivity(), EditProfileContract.View {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var presenter: EditProfileContract.Presenter
    private lateinit var sessionManager: SessionManager
    private lateinit var imageLoader: ImageLoader
    private lateinit var errorHandler: ErrorHandler
    private lateinit var profileValidator: ProfileValidator

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.imgProfile.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDependencies()
        setupViews()
        presenter.loadUserProfile()
    }

    private fun initDependencies() {
        sessionManager = SessionManager(this)
        val userRepository = UserRepository(sessionManager)
        presenter = EditProfilePresenter(this, userRepository)
    }

    private fun setupViews() {
        binding.btnChangePhoto.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            handleSaveProfile()
        }
    }

    private fun handleSaveProfile() {
        val name = binding.edtName.text.toString().trim()
        val lastName = binding.edtLastName.text.toString().trim()
        val ageText = binding.edtAge.text.toString().trim()

        val validationResult = profileValidator.validateProfileData(name, lastName, ageText)

        if (!validationResult.isValid) {
            showError(validationResult.errorMessage)
            return
        }

        val age = ageText.toInt()
        val imagePart = ImagePartFactory.createImagePart(this, selectedImageUri)

        presenter.updateUserProfile(name, lastName, age, imagePart)
    }

    override fun showUserData(user: User) {
        binding.edtName.setText(user.name)
        binding.edtLastName.setText(user.last_name)
        binding.edtAge.setText(user.age.toString())

        imageLoader.loadProfileImage(user.profile_image_url, binding.imgProfile)
    }

    override fun showSuccess(message: String) {
        errorHandler.showSuccess(message)
        finish()
    }

    override fun showError(message: String) {
        errorHandler.showError(message)
    }

    override fun goBackProfile() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::presenter.isInitialized) {
            presenter.onDestroy()
        }
    }
}

