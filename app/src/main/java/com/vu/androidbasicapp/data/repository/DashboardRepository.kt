package com.vu.androidbasicapp.data.repository

import com.vu.androidbasicapp.data.model.DashboardResponse

interface DashboardRepository {
    suspend fun getDashboard(keypass: String): Result<DashboardResponse>
}