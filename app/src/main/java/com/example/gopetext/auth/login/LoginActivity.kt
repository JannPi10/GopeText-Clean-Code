package com.example.gopetext.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gopetext.R
import com.example.gopetext.auth.home.HomeActivity
import com.example.gopetext.auth.register.RegisterActivity
import com.example.gopetext.data.api.RetrofitClient
import com.example.gopetext.data.storage.SessionManager


class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa campos
        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        val btnIniciarSesion = findViewById<Button>(R.id.btnEntrar)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        // Inicializa SessionManager y Presenter
        sessionManager = SessionManager(applicationContext)
        presenter = LoginPresenter(this, RetrofitClient.authService, sessionManager)

        btnIniciarSesion.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            presenter.login(email, password)
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }
}
