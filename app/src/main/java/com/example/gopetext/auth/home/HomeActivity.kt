package com.example.gopetext.auth.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gopetext.R
import com.example.gopetext.auth.login.LoginActivity
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ApiClient.init(applicationContext)

        sessionManager = SessionManager(this)

        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_salir -> {
                logoutUser() // Llamamos la función mejorada
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authService = ApiClient.retrofit.create(AuthService::class.java)
                val response = authService.logout()

                if (response.isSuccessful) {
                    Log.d("Logout", "Sesión cerrada en servidor")
                } else {
                    Log.e("Logout", "Error al cerrar sesión: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("Logout", "Error inesperado: ${e.localizedMessage}")
            }

            // Siempre limpiamos la sesión local y volvemos al login
            sessionManager.clearSession()
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
