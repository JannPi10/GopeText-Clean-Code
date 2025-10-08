package com.example.gopetext.auth.home.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.repository.RemoteGroupRepository
import com.example.gopetext.data.repository.RemoteUsersRepository
import com.example.gopetext.data.storage.SessionManager
import com.example.gopetext.databinding.FragmentCreateGroupBinding

class CreateGroupFragment : Fragment(), CreateGroupContract.View {

    private lateinit var binding: FragmentCreateGroupBinding
    private lateinit var presenter: CreateGroupContract.Presenter
    private lateinit var adapter: UserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateGroupBinding.inflate(inflater, container, false)

        val sessionManager = SessionManager(requireContext())
        presenter = CreateGroupPresenter(
            this,
            RemoteUsersRepository(ApiClient.getService()),
            RemoteGroupRepository(ApiClient.getService()),
            sessionManager
        )

        adapter = UserAdapter(emptyList())
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        binding.btnCreateGroup.setOnClickListener {
            val groupName = binding.etGroupName.text.toString().trim()
            val selectedUsers = adapter.getSelectedUsers()
            presenter.createGroup(groupName, selectedUsers)
        }

        presenter.loadUsers()
        return binding.root
    }

    override fun showUsers(users: List<UserChat>) {
        adapter.setUsers(users)
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateBack() {
        parentFragmentManager.setFragmentResult("group_created", Bundle())
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }
}