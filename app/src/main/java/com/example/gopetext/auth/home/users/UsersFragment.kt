package com.example.gopetext.auth.home.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gopetext.auth.home.users.chat.ChatActivity
import android.view.View
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.databinding.FragmentUserBinding

class UsersFragment : Fragment(), UsersContract.View {

    private lateinit var binding: FragmentUserBinding
    private lateinit var presenter: UsersContract.Presenter
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        presenter = UsersPresenter(this)

        adapter = UsersAdapter(emptyList()) { user ->
            presenter.onUserClicked(user)
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        presenter.loadUsers()

        return binding.root
    }

    override fun showUsers(users: List<UserChat>) {
        Log.d("UsersFragment", "Usuarios recibidos: ${users.size}")
        adapter.updateList(users)
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun navigateToChat(chatId: Int, user: UserChat) {
        val intent = Intent(requireContext(), ChatActivity::class.java).apply {
            putExtra("chat_id", chatId)
            putExtra("receiver_id", user.id)
            putExtra("receiver_name", user.name)
            putExtra("receiver_image_url", user.profile_image_url)
        }
        startActivity(intent)
    }
}

