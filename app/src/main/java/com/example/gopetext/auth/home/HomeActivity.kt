package com.example.gopetext.auth.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gopetext.R
import com.example.gopetext.auth.home.fragments.chats.ChatsFragment
import com.example.gopetext.auth.home.fragments.groups.CreateGroupFragment
import com.example.gopetext.auth.home.fragments.profile.ProfileFragment
import com.example.gopetext.auth.login.LoginActivity
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.storage.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var sessionManager: SessionManager
    private lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ApiClient.init(applicationContext)

        sessionManager = SessionManager(this)

        presenter = HomePresenter(
            this,
            ApiClient.getService(),
            sessionManager
        )

        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chats -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ChatsFragment())
                        .commit()
                    true
                }
                R.id.nav_group -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CreateGroupFragment())
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileFragment())
                        .commit()
                    Log.d("SessionManager", "Token actual: ${sessionManager.getAccessToken()}")
                    true
                }
                R.id.logout -> {
                    presenter.logout()
                    true
                }
                else -> false
            }
        }

    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun navigateToProfile() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun showLogoutMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }
}
