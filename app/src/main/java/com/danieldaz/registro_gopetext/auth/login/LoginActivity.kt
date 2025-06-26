package com.danieldaz.registro_gopetext.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.danieldaz.registro_gopetext.R
import com.danieldaz.registro_gopetext.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

    btnRegistrarse.setOnClickListener {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    }
}