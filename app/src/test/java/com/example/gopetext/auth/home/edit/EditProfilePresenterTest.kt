package com.example.gopetext.auth.home.edit

import com.example.gopetext.data.api.UserSingleResponse
import com.example.gopetext.data.model.User
import com.example.gopetext.data.repository.UserRepository
import com.example.gopetext.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MultipartBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfilePresenterTest {

    private lateinit var presenter: EditProfilePresenter
    private lateinit var view: EditProfileContract.View
    private lateinit var userRepository: UserRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyUser = User(
        id = 1,
        name = "Daniel",
        last_name = "Daza",
        email = "daniel@example.com",
        age = 25,
        profile_image_url = "https://example.com/profile.jpg"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        view = mock()
        userRepository = mock()
        presenter = EditProfilePresenter(
            view,
            userRepository,
            mainDispatcher = testDispatcher,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUserProfile should show user data on success`() = runTest {
        val response = Result.Success(UserSingleResponse(dummyUser))
        whenever(userRepository.getUserProfile()).thenReturn(response)

        presenter.loadUserProfile()

        verify(view).showUserData(dummyUser)
        verify(view, never()).showError(any())
    }

    @Test
    fun `loadUserProfile should show error on failure`() = runTest {
        val errorMsg = "Error al cargar perfil"
        whenever(userRepository.getUserProfile()).thenReturn(Result.Error(errorMsg))

        presenter.loadUserProfile()

        verify(view).showError(errorMsg)
        verify(view, never()).showUserData(any())
    }

    private fun presenterWithUser(user: User): EditProfilePresenter {
        val p = EditProfilePresenter(view, userRepository)
        p.currentUser = user
        return p
    }

    @Test
    fun `updateUserProfile should show no changes when data is identical`() = runTest {
        val updatedPresenter = presenterWithUser(dummyUser)

        updatedPresenter.updateUserProfile(
            dummyUser.name,
            dummyUser.last_name,
            dummyUser.age,
            null
        )

        verify(view).showSuccess("No se hicieron cambios.")
        verify(view).goBackProfile()
        verify(view, never()).showError(any())
    }

    @Test
    fun `onDestroy cancels coroutine job`() {
        presenter.onDestroy()
    }
}
