package com.vu.androidbasicapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vu.androidbasicapp.MainDispatcherRule
import com.vu.androidbasicapp.data.model.LoginResponse
import com.vu.androidbasicapp.data.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAuthRepository(
        private val result: Result<LoginResponse>
    ) : AuthRepository {

        var capturedUsername: String? = null
        var capturedPassword: String? = null

        override suspend fun login(
            username: String,
            password: String
        ): Result<LoginResponse> {
            capturedUsername = username
            capturedPassword = password
            return result
        }
    }

    @Test
    fun login_withBlankUsername_setsValidationError() = runTest {
        val repository = FakeAuthRepository(
            Result.success(LoginResponse(keypass = "courses"))
        )
        val viewModel = LoginViewModel(repository)

        viewModel.login(
            username = "",
            password = "Justin"
        )

        val state = viewModel.uiState.value

        assertFalse(state!!.isLoading)
        assertEquals("Username and password are required.", state.errorMessage)
        assertNull(state.keypass)
    }

    @Test
    fun login_withValidCredentials_setsKeypass() = runTest {
        val repository = FakeAuthRepository(
            Result.success(LoginResponse(keypass = "courses"))
        )
        val viewModel = LoginViewModel(repository)

        viewModel.login(
            username = " s8115784 ",
            password = " Justin "
        )

        val state = viewModel.uiState.value

        assertEquals("s8115784", repository.capturedUsername)
        assertEquals("Justin", repository.capturedPassword)
        assertFalse(state!!.isLoading)
        assertNull(state.errorMessage)
        assertEquals("courses", state.keypass)
    }

    @Test
    fun login_whenRepositoryFails_setsErrorMessage() = runTest {
        val repository = FakeAuthRepository(
            Result.failure(Exception("Login failed. Please check your username and password."))
        )
        val viewModel = LoginViewModel(repository)

        viewModel.login(
            username = "s8115784",
            password = "Justin"
        )

        val state = viewModel.uiState.value

        assertFalse(state!!.isLoading)
        assertEquals(
            "Login failed. Please check your username and password.",
            state.errorMessage
        )
        assertNull(state.keypass)
    }

    @Test
    fun clearNavigationState_removesKeypass() = runTest {
        val repository = FakeAuthRepository(
            Result.success(LoginResponse(keypass = "courses"))
        )
        val viewModel = LoginViewModel(repository)

        viewModel.login(
            username = "s8115784",
            password = "Justin"
        )

        viewModel.clearNavigationState()

        val state = viewModel.uiState.value

        assertFalse(state!!.isLoading)
        assertNull(state.keypass)
    }
}