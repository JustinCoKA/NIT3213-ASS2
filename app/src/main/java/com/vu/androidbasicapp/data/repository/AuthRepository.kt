package com.vu.androidbasicapp.data.repository

import com.vu.androidbasicapp.data.model.LoginResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoginResponse>
}