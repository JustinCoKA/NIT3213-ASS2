package com.vu.androidbasicapp.data.repository

import com.vu.androidbasicapp.data.model.LoginRequest
import com.vu.androidbasicapp.data.model.LoginResponse
import com.vu.androidbasicapp.data.remote.Nit3213ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: Nit3213ApiService
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = apiService.login(
                LoginRequest(
                    username = username,
                    password = password
                )
            )
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception("Login failed. Please check your username and password."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}