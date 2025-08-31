package com.example.gopetext.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.gopetext.auth.home.HomeActivity
import com.example.gopetext.auth.login.validation.EmailValidator
import com.example.gopetext.auth.login.validation.PasswordValidator
import com.example.gopetext.auth.register.RegisterActivity
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.repository.RemoteAuthRepository
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginContract.Presenter
    private lateinit var sessionManager: SessionManager

    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.init(applicationContext)
        sessionManager = SessionManager(applicationContext)
        val repository = RemoteAuthRepository(ApiClient.getService())
        presenter = LoginPresenter(this, repository, sessionManager)

        presenter.checkSession()

        binding.btnEnter.setOnClickListener {
            if (validateInputs()) {
                val email = binding.etEmail.text?.toString()?.trim().orEmpty()
                val password = binding.etPassword.text?.toString().orEmpty()
                presenter.login(email, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.etEmail.doAfterTextChanged { binding.tilEmail.error = null }
        binding.etPassword.doAfterTextChanged { binding.tilPassword.error = null }
    }

    private fun validateInputs(): Boolean {
        val emailText = binding.etEmail.text?.toString()?.trim().orEmpty()
        val passwordText = binding.etPassword.text?.toString().orEmpty()

        val emailError = emailValidator.validate(emailText)
        val passwordError = passwordValidator.validate(passwordText)

        binding.tilEmail.error = emailError
        binding.tilPassword.error = passwordError

        return emailError == null && passwordError == null
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
