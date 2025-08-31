package com.example.gopetext.auth.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gopetext.R
import com.example.gopetext.auth.home.fragments.chats.ChatsListFragment
import com.example.gopetext.auth.home.fragments.groups.CreateGroupFragment
import com.example.gopetext.auth.home.fragments.profile.ProfileFragment
import com.example.gopetext.auth.home.users.UsersFragment
import com.example.gopetext.auth.login.LoginActivity
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.repository.RemoteAccountRepository
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.init(applicationContext)

        sessionManager = SessionManager(this)
        presenter = HomePresenter(
            this,
            RemoteAccountRepository(ApiClient.getService()),
            sessionManager
        )

        setSupportActionBar(binding.myToolbar)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chats -> replaceFragment(ChatsListFragment())
                R.id.nav_group -> replaceFragment(CreateGroupFragment())
                R.id.nav_users -> replaceFragment(UsersFragment())
                R.id.nav_profile -> { presenter.profile(); true }
                R.id.logout -> { presenter.logout(); true }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_chats
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun navigateToProfile() {
        replaceFragment(ProfileFragment())
    }

    override fun showLogoutMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }
}
