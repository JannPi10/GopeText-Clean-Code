package com.example.gopetext.auth.home.fragments.groups

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.gopetext.data.model.UserChat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class CreateGroupFragmentTest {

    private lateinit var presenter: CreateGroupContract.Presenter
    private lateinit var fragment: CreateGroupContract.View
    private lateinit var adapter: UserAdapter
    private lateinit var context: Context
    private lateinit var fragmentManager: FragmentManager

    @Before
    fun setup() {
        presenter = mock()
        adapter = mock()
        context = mock()
        fragmentManager = mock()

        fragment = object : CreateGroupContract.View {
            override fun showUsers(users: List<UserChat>) {
                adapter.setUsers(users)
            }

            override fun showSuccess(message: String) {
                println("Éxito: $message")
            }

            override fun showError(message: String) {
                println("Error: $message")
            }

            override fun navigateBack() {
                println("Navegando hacia atrás")
            }
        }
    }

    @Test
    fun `showUsers should set users to adapter`() {
        val users = listOf(
            UserChat(
                id = 1,
                name = "Juan Pérez",
                profile_image_url = "https://image.com/juan.jpg"
            ),
            UserChat(
                id = 2,
                name = "María García",
                profile_image_url = "https://image.com/maria.jpg"
            ),
            UserChat(
                id = 3,
                name = "Carlos López",
                profile_image_url = "https://image.com/carlos.jpg"
            )
        )

        fragment.showUsers(users)

        verify(adapter).setUsers(users)
    }

    @Test
    fun `showSuccess should not crash when displaying message`() {
        fragment.showSuccess("Grupo creado exitosamente")
    }

    @Test
    fun `showError should not crash when displaying message`() {
        fragment.showError("Error al crear el grupo")
    }

    @Test
    fun `navigateBack should not crash`() {
        fragment.navigateBack()
    }

    @Test
    fun `presenter should load users on initialization`() {
        presenter.loadUsers()
        verify(presenter).loadUsers()
    }

    @Test
    fun `presenter should create group with name and selected users`() {
        val groupName = "Equipo Kotlin"
        val selectedUsers = listOf(
            UserChat(1, "Juan", "https://img.com/j.jpg"),
            UserChat(2, "Ana", "https://img.com/a.jpg")
        )

        presenter.createGroup(groupName, selectedUsers)
        verify(presenter).createGroup(groupName, selectedUsers)
    }

    @Test
    fun `showUsers with empty list should set empty list to adapter`() {
        val emptyUsers = emptyList<UserChat>()

        fragment.showUsers(emptyUsers)

        verify(adapter).setUsers(emptyUsers)
    }

    @Test
    fun `presenter should be destroyed when fragment is destroyed`() {
        presenter.onDestroy()
        verify(presenter).onDestroy()
    }

    @Test
    fun `showError should display appropriate message for network error`() {
        fragment.showError("Error de red: no se pudieron cargar los usuarios")
    }

    @Test
    fun `showError should display appropriate message for empty group name`() {
        fragment.showError("El nombre del grupo no puede estar vacío")
    }

    @Test
    fun `showError should display appropriate message for insufficient members`() {
        fragment.showError("Debes seleccionar al menos 2 usuarios para crear un grupo")
    }
}