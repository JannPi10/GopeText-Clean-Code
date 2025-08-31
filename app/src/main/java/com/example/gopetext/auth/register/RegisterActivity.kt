package com.example.gopetext.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.gopetext.auth.login.LoginActivity
import com.example.gopetext.auth.login.validation.EmailValidator
import com.example.gopetext.auth.login.validation.PasswordValidator
import com.example.gopetext.auth.register.validation.AgeRangeValidator
import com.example.gopetext.auth.register.validation.NonEmptyTextValidator
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.repository.RemoteUserRegistrationRepository
import com.example.gopetext.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterContract.Presenter

    private val firstNameValidator = NonEmptyTextValidator(fieldLabel = "First name", minLength = 2)
    private val lastNameValidator = NonEmptyTextValidator(fieldLabel = "Last name", minLength = 2)
    private val ageValidator = AgeRangeValidator(min = 1, max = 120)
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = RemoteUserRegistrationRepository(ApiClient.getService())
        presenter = RegisterPresenter(this, repository)

        binding.btnRegister.setOnClickListener {
            if (validateInputs()) {
                val firstName = binding.etName.text?.toString()?.trim().orEmpty()
                val lastName = binding.etLastName.text?.toString()?.trim().orEmpty()
                val age = binding.etAge.text?.toString()?.trim().orEmpty().toInt()
                val email = binding.etEmailAddress.text?.toString()?.trim().orEmpty()
                val password = binding.etPassword.text?.toString().orEmpty()
                val confirmPassword = binding.etConfirmPassword.text?.toString().orEmpty()
                presenter.register(firstName, lastName, age, email, password, confirmPassword)
            }
        }

        binding.btnBackToLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.etName.doAfterTextChanged { binding.etName.error = null }
        binding.etLastName.doAfterTextChanged { binding.etLastName.error = null }
        binding.etAge.doAfterTextChanged { binding.etAge.error = null }
        binding.etEmailAddress.doAfterTextChanged { binding.etEmailAddress.error = null }
        binding.etPassword.doAfterTextChanged { binding.etPassword.error = null }
        binding.etConfirmPassword.doAfterTextChanged { binding.etConfirmPassword.error = null }
    }

    private fun validateInputs(): Boolean {
        val firstNameText = binding.etName.text?.toString()?.trim().orEmpty()
        val lastNameText = binding.etLastName.text?.toString()?.trim().orEmpty()
        val ageText = binding.etAge.text?.toString()?.trim().orEmpty()
        val emailText = binding.etEmailAddress.text?.toString()?.trim().orEmpty()
        val passwordText = binding.etPassword.text?.toString().orEmpty()
        val confirmPasswordText = binding.etConfirmPassword.text?.toString().orEmpty()

        val firstNameError = firstNameValidator.validate(firstNameText)
        val lastNameError = lastNameValidator.validate(lastNameText)
        val ageError = ageValidator.validate(ageText)
        val emailError = emailValidator.validate(emailText)
        val passwordError = passwordValidator.validate(passwordText)
        val confirmPasswordError = if (confirmPasswordText != passwordText) "Contraseñas no coinciden" else null

        binding.etName.error = firstNameError
        binding.etLastName.error = lastNameError
        binding.etAge.error = ageError
        binding.etEmailAddress.error = emailError
        binding.etPassword.error = passwordError
        binding.etConfirmPassword.error = confirmPasswordError

        return listOf(
            firstNameError,
            lastNameError,
            ageError,
            emailError,
            passwordError,
            confirmPasswordError
        ).all { it == null }
    }

    override fun showRegisterSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showRegisterError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
