package com.example.gopetext.auth.home.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopetext.R
import com.example.gopetext.data.api.ApiClient
import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.model.Contact
import com.example.gopetext.utils.toast

class ChatsListFragment : Fragment(), ChatsListContract.View {

    private lateinit var presenter: ChatsListContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatsListAdapter
    private lateinit var navigator: ChatsNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ Dependencias inyectadas mediante interfaces
        presenter = ChatsListPresenter(this, ApiClient.createService<ChatService>())
        navigator = DefaultChatsNavigator(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupFragmentListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadChats()
    }

    override fun showChats(chats: List<Contact>) {
        adapter.submitList(chats.toList()) // Crear copia para evitar problemas de diff
    }

    override fun showError(message: String) {
        requireContext().toast(message)
    }

    override fun showEmptyState() {
        requireContext().toast("No tienes chats aún")
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerChats)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ChatsListAdapter { contact ->
            navigator.navigateToChat(contact)
        }
        recyclerView.adapter = adapter
    }

    private fun setupFragmentListeners() {
        parentFragmentManager.setFragmentResultListener("group_created", viewLifecycleOwner) { _, _ ->
            presenter.loadChats()
        }
    }
}
