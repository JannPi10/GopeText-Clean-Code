package com.example.gopetext.auth.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gopetext.R
import android.content.Intent
import android.widget.Button
import com.example.gopetext.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}