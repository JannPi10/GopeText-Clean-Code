package com.example.gopetext.auth.home.fragments.chats

import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.ChatsResponse
import com.example.gopetext.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ChatsListPresenterTest {

    private lateinit var presenter: ChatsListPresenter
    private lateinit var view: ChatsListContract.View
    private lateinit var chatService: ChatService
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        view = mock()
        chatService = mock()
        presenter = ChatsListPresenter(view, chatService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadChats success with contacts shows chats`() = runTest {
        val contacts = listOf(
            Contact(1, false, "John Doe", null, 1, null),
            Contact(2, true, "Jane Smith", null, null, 5)
        )
        val response = ChatsResponse(contacts)

        whenever(chatService.getChats()).thenReturn(Response.success(response))

        presenter.loadChats()

        verify(view).showChats(contacts)
    }

    @Test
    fun `loadChats success with empty list shows empty state`() = runTest {
        val response = ChatsResponse(emptyList())

        whenever(chatService.getChats()).thenReturn(Response.success(response))

        presenter.loadChats()

        verify(view).showEmptyState()
    }

    @Test
    fun `loadChats http error shows error message`() = runTest {
        whenever(chatService.getChats()).thenReturn(Response.error(500, mock()))

        presenter.loadChats()

        verify(view).showError("Error al cargar chats")
    }

    @Test
    fun `loadChats network error shows error message`() = runTest {
        whenever(chatService.getChats()).thenThrow(RuntimeException("Network error"))

        presenter.loadChats()

        verify(view).showError("Error de red")
    }
}