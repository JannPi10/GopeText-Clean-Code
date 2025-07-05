package com.example.gopetext.auth.home.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.AuthService
import com.example.gopetext.data.model.Contact

class ChatsFragment : Fragment(), ChatsContract.View {

    private lateinit var presenter: ChatsContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerChats)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ChatsAdapter { contact ->
            Toast.makeText(requireContext(), "Chat con ${contact.name}", Toast.LENGTH_SHORT).show()
            // Aquí tu compa abrirá la conversación
        }

        recyclerView.adapter = adapter

        presenter = ChatsPresenter(this, ApiClient.getService())
        presenter.loadChats()
    }

    override fun showChats(chats: List<Contact>) {
        adapter.submitList(chats)
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyState() {
        Toast.makeText(requireContext(), "No tienes chats aún", Toast.LENGTH_SHORT).show()
    }
}
