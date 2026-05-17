package com.vu.androidbasicapp.data.remote

import com.vu.androidbasicapp.data.model.DashboardResponse
import com.vu.androidbasicapp.data.model.LoginRequest
import com.vu.androidbasicapp.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Nit3213ApiService {

    @POST("sydney/auth")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): DashboardResponse
}