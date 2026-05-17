package com.vu.androidbasicapp.data.repository

import com.vu.androidbasicapp.data.model.DashboardResponse
import com.vu.androidbasicapp.data.remote.Nit3213ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: Nit3213ApiService
) : DashboardRepository {

    override suspend fun getDashboard(
        keypass: String
    ): Result<DashboardResponse> {
        return try {
            val response = apiService.getDashboard(keypass)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception("Failed to load dashboard data."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}