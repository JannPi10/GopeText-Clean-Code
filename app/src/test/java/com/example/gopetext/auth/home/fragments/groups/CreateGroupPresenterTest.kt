package com.example.gopetext.auth.home.fragments.groups

import com.example.gopetext.core.ApiResult
import com.example.gopetext.data.model.UserChat
import com.example.gopetext.data.repository.GroupRepository
import com.example.gopetext.data.repository.UsersRepository
import com.example.gopetext.data.storage.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CreateGroupPresenterTest {

    private lateinit var presenter: CreateGroupPresenter
    private lateinit var view: CreateGroupContract.View
    private lateinit var usersRepository: UsersRepository
    private lateinit var groupRepository: GroupRepository
    private lateinit var sessionManager: SessionManager
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        view = mock()
        usersRepository = mock()
        groupRepository = mock()
        sessionManager = mock()
        presenter = CreateGroupPresenter(view, usersRepository, groupRepository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
//
//    @Test
//    fun `loadUsers success filters current user and shows users`() = runTest {
//        val currentUserId = 1
//        val users = listOf(
//            UserChat(1, "Current User", null),
//            UserChat(2, "Other User", null),
//            UserChat(3, "Another User", null)
//        )
//
//        whenever(sessionManager.getUserId()).thenReturn(currentUserId)
//        whenever(usersRepository.getAllUsersForChat()).thenReturn(ApiResult.Success(users))
//
//        presenter.loadUsers()
//
//        verify(view).showUsers(listOf(users[1], users[2]))
//    }
//
//    @Test
//    fun `loadUsers http error shows error message`() = runTest {
//        whenever(usersRepository.getAllUsersForChat()).thenReturn(ApiResult.HttpError(500, "Server error"))
//
//        presenter.loadUsers()
//
//        verify(view).showError("Unable to load users")
//    }
//
//    @Test
//    fun `loadUsers network error shows network message`() = runTest {
//        val errorMessage = "No internet connection"
//        whenever(usersRepository.getAllUsersForChat()).thenReturn(ApiResult.NetworkError(errorMessage))
//
//        presenter.loadUsers()
//
//        verify(view).showError(errorMessage)
//    }

    @Test
    fun `createGroup with blank name shows error`() = runTest {
        val users = listOf(UserChat(1, "User", null))

        presenter.createGroup("", users)

        verify(view).showError("Enter a group name")
        verify(groupRepository, never()).createGroup(any(), any())
    }

//    @Test
//    fun `createGroup with empty users shows error`() = runTest {
//        presenter.createGroup("My Group", emptyList())
//
//        verify(view).showError("Select at least one person")
//        verify(groupRepository, never()).createGroup(any(), any())
//    }
//
//    @Test
//    fun `createGroup success shows success and navigates back`() = runTest {
//        val groupName = "Test Group"
//        val users = listOf(
//            UserChat(2, "User 1", null),
//            UserChat(3, "User 2", null)
//        )
//
//        whenever(groupRepository.createGroup(groupName, listOf(2, 3)))
//            .thenReturn(ApiResult.Success(Unit))
//
//        presenter.createGroup(groupName, users)
//
//        verify(view).showSuccess("Group created successfully")
//        verify(view).navigateBack()
//    }

//    @Test
//    fun `createGroup http error shows error message`() = runTest {
//        val users = listOf(UserChat(2, "User", null))
//
//        whenever(groupRepository.createGroup(any(), any()))
//            .thenReturn(ApiResult.HttpError(400, "Bad request"))
//
//        presenter.createGroup("Group", users)
//
//        verify(view).showError("Unable to create group")
//    }
//
//    @Test
//    fun `createGroup network error shows network message`() = runTest {
//        val errorMessage = "Connection timeout"
//        val users = listOf(UserChat(2, "User", null))
//
//        whenever(groupRepository.createGroup(any(), any()))
//            .thenReturn(ApiResult.NetworkError(errorMessage))
//
//        presenter.createGroup("Group", users)
//
//        verify(view).showError(errorMessage)
//    }
}