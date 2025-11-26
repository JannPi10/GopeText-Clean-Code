package com.example.gopetext.auth.home.fragments.chats

import com.example.gopetext.data.api.ChatService
import com.example.gopetext.data.api.ChatsResponse
import com.example.gopetext.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ChatsListPresenterTest {

    private lateinit var presenter: ChatsListPresenter
    private lateinit var view: ChatsListContract.View
    private lateinit var chatService: ChatService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        view = mock()
        chatService = mock()
        presenter = ChatsListPresenter(view, chatService, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        presenter.onDestroy()
    }

    @Test
    fun loadChatsSuccessWithContactsShowsChats() = runTest {
        // Given
        val contacts = listOf(
            Contact(id = 1, is_group = false, name = "John Doe", profile_image_url = null, user_id = 1, members_count = null),
            Contact(id = 2, is_group = true, name = "Jane Smith", profile_image_url = null, user_id = null, members_count = 5)
        )
        val response = Response.success(ChatsResponse(contacts))

        whenever(chatService.getChats()).thenReturn(response)

        // When
        presenter.loadChats()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(view).showChats(contacts)
        verify(view, never()).showError(any())
        verify(view, never()).showEmptyState()
    }

    @Test
    fun loadChatsSuccessWithEmptyListShowsEmptyState() = runTest {
        // Given
        val response = Response.success(ChatsResponse(emptyList()))
        whenever(chatService.getChats()).thenReturn(response)

        // When
        presenter.loadChats()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(view).showEmptyState()
        verify(view, never()).showChats(any())
        verify(view, never()).showError(any())
    }

    @Test
    fun loadChatsHttpErrorShowsErrorMessage() = runTest {
        // Given
        val errorBody = "Internal Server Error".toResponseBody("application/json".toMediaType())
        whenever(chatService.getChats()).thenReturn(Response.error(500, errorBody))

        // When
        presenter.loadChats()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(view).showError("Error 500: Internal Server Error")
        verify(view, never()).showChats(any())
        verify(view, never()).showEmptyState()
    }

    @Test
    fun loadChatsNetworkErrorShowsErrorMessage() = runTest {
        // Given
        whenever(chatService.getChats()).thenThrow(RuntimeException("Network error"))

        // When
        presenter.loadChats()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(view).showError("Error al obtener los chats: Network error")
        verify(view, never()).showChats(any())
        verify(view, never()).showEmptyState()
    }
}
