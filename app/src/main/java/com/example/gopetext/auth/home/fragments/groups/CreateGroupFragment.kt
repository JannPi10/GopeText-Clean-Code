package com.example.gopetext.auth.home.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.storage.SessionManager

class CreateGroupFragment : Fragment(), CreateGroupContract.View {

    private lateinit var presenter: CreateGroupContract.Presenter
    private lateinit var sessionManager: SessionManager
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionManager = SessionManager(requireContext())
        presenter = CreateGroupPresenter(this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvUsers)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateGroup)

        userAdapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = userAdapter

        presenter.loadUsers()

        btnCreate.setOnClickListener {
            val selectedUsers = userAdapter.getSelectedUsers()
            presenter.createGroup(selectedUsers)
        }
    }

    override fun showUsers(users: List<UserChat>) {
        userAdapter.setUsers(users)
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
