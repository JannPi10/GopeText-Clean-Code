package com.example.gopetext.auth.home.fragments.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.auth.home.fragments.chats.ChatsListFragment
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.storage.SessionManager

class CreateGroupFragment : Fragment(), CreateGroupContract.View {

    private lateinit var presenter: CreateGroupContract.Presenter
    private lateinit var sessionManager: SessionManager
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_create_group, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        presenter = CreateGroupPresenter(this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvUsers)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateGroup)
        val etGroupName = view.findViewById<EditText>(R.id.etGroupName)

        userAdapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = userAdapter

        val currentUserId = sessionManager.getUserId()
        presenter.loadUsers(currentUserId)

        btnCreate.setOnClickListener {
            val selectedUsers = userAdapter.getSelectedUsers()
            Log.d("CreateGroupFragment", "Usuarios seleccionados para grupo: ${selectedUsers.map { it.name }}")
            val groupName = etGroupName.text.toString().trim()
            presenter.createGroup(groupName, selectedUsers)
        }
    }

    override fun showUsers(users: List<UserChat>) {
        Log.d("CreateGroupFragment", "Mostrando ${users.size} usuarios en RecyclerView")
        userAdapter.setUsers(users)
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        // Notificamos a ChatsListFragment que se creó un grupo
        parentFragmentManager.setFragmentResult("group_created", Bundle())

        // Volvemos al fragmento anterior (ChatsListFragment ya está en el backstack)
        parentFragmentManager.popBackStack()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

