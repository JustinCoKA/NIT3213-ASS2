package com.vu.androidbasicapp.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val keypass: String? = null
)