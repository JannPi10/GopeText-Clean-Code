package com.example.gopetext.auth.home.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gopetext.auth.home.users.chat.ChatSingleActivity
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.repository.RemoteChatRepository
import com.example.gopetext.data.repository.RemoteUsersRepository
import com.example.gopetext.databinding.FragmentUserBinding

class UsersFragment : Fragment(), UsersContract.View {

    private lateinit var binding: FragmentUserBinding
    private lateinit var presenter: UsersContract.Presenter
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        presenter = UsersPresenter(
            this,
            RemoteUsersRepository(ApiClient.getService()),
            RemoteChatRepository(ApiClient.createService<ChatService>())
        )

        adapter = UsersAdapter(emptyList()) { user -> presenter.onUserClicked(user) }
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        presenter.loadUsers()
        return binding.root
    }

    override fun showUsers(users: List<UserChat>) {
        adapter.updateList(users)
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun navigateToChat(chatId: Int, user: UserChat) {
        val intent = Intent(requireContext(), ChatSingleActivity::class.java).apply {
            putExtra("chatId", chatId)
            putExtra("chatName", user.name)
            putExtra("isGroup", false)
        }
        startActivity(intent)
    }
}
