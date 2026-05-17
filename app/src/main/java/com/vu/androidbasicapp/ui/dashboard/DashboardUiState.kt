package com.vu.androidbasicapp.ui.dashboard

import com.vu.androidbasicapp.data.model.Entity

data class DashboardUiState(
    val isLoading: Boolean = false,
    val entities: List<Entity> = emptyList(),
    val entityTotal: Int = 0,
    val errorMessage: String? = null
)