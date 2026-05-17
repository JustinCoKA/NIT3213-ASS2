package com.vu.androidbasicapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vu.androidbasicapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableLiveData(LoginUiState())
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        val trimmedUsername = username.trim()
        val trimmedPassword = password.trim()

        if (trimmedUsername.isBlank() || trimmedPassword.isBlank()) {
            _uiState.value = LoginUiState(
                isLoading = false,
                errorMessage = "Username and password are required."
            )
            return
        }

        _uiState.value = LoginUiState(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.login(
                username = trimmedUsername,
                password = trimmedPassword
            )

            _uiState.value = result.fold(
                onSuccess = { response ->
                    LoginUiState(
                        isLoading = false,
                        keypass = response.keypass
                    )
                },
                onFailure = { error ->
                    LoginUiState(
                        isLoading = false,
                        errorMessage = error.message ?: "Login failed."
                    )
                }
            )
        }
    }

    fun clearNavigationState() {
        _uiState.value = _uiState.value?.copy(keypass = null)
    }
}