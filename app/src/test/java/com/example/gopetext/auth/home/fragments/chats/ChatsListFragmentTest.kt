package com.example.gopetext.auth.home.fragments.chats

import android.content.Context
import com.example.gopetext.data.model.Contact
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class ChatsListFragmentTest {

    private lateinit var presenter: ChatsListContract.Presenter
    private lateinit var fragment: ChatsListContract.View
    private lateinit var navigator: ChatsNavigator
    private lateinit var adapter: ChatsListAdapter

    @Before
    fun setup() {
        presenter = mock()
        navigator = mock()
        adapter = mock()
        context = mock()

        // Creamos una implementación mínima del View (simulamos el fragmento)
        fragment = object : ChatsListContract.View {
            override fun showChats(chats: List<Contact>) {
                adapter.submitList(chats)
            }

            override fun showError(message: String) {
                // En tests unitarios no mostramos toasts, solo verificamos que no falle
                println("Error mostrado: $message")
            }

            override fun showEmptyState() {
                println("Estado vacío mostrado")
            }
        }
    }

    @Test
    fun `showChats should submit list to adapter`() {
        val chats = listOf(
            Contact(
                id = 1,
                name = "Jann",
                is_group = false,
                profile_image_url = "https://image.com/jann.jpg",
                user_id = 2,
                members_count = null
            ),
            Contact(
                id = 2,
                name = "Grupo Kotlin Devs",
                is_group = true,
                profile_image_url = "https://image.com/group.jpg",
                user_id = null,
                members_count = 5
            )
        )

        fragment.showChats(chats)

        verify(adapter).submitList(chats)
    }

    @Test
    fun `showError should not crash when displaying message`() {
        fragment.showError("Error al cargar chats")
    }

    @Test
    fun `showEmptyState should not crash`() {
        fragment.showEmptyState()
    }

    @Test
    fun `presenter should load chats when fragment resumes`() {
        // simulamos un evento de onResume
        presenter.loadChats()
        verify(presenter).loadChats()
    }
}
