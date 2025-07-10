package com.example.gopetext.auth.home.fragments.chats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.auth.home.users.chat.ChatSingleActivity
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.model.Contact

class ChatsListFragment : Fragment(), ChatsListContract.View {

    private lateinit var presenter: ChatsListContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializamos el presenter temprano para evitar crash
        presenter = ChatsListPresenter(this, ApiClient.retrofit.create(ChatService::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerChats)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ChatsListAdapter { contact ->
            val intent = Intent(requireContext(), ChatSingleActivity::class.java)
            intent.putExtra("chatId", contact.id)
            intent.putExtra("chatName", contact.name)
            intent.putExtra("isGroup", contact.is_group)
            requireContext().startActivity(intent)
        }

        recyclerView.adapter = adapter

        // 🔥 Escuchamos si se creó un grupo nuevo
        parentFragmentManager.setFragmentResultListener("group_created", viewLifecycleOwner) { _, _ ->
            Log.d("ChatsFragment", "Se ha creado un grupo nuevo, recargando chats")
            presenter.loadChats()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ChatsFragment", "Volviendo a cargar los chats")
        presenter.loadChats()
    }

    override fun showChats(chats: List<Contact>) {
        Log.d("ChatsFragment", "Mostrando ${chats.size} chats")
        adapter.submitList(null) // workaround por si DiffUtil no actualiza bien
        adapter.submitList(chats)
    }

    override fun showError(message: String) {
        Log.e("ChatsFragment", "Error: $message")
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyState() {
        Log.d("ChatsFragment", "No hay chats para mostrar")
        Toast.makeText(requireContext(), "No tienes chats aún", Toast.LENGTH_SHORT).show()
    }
}
