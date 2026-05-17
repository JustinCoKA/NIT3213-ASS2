package com.vu.androidbasicapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vu.androidbasicapp.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableLiveData(DashboardUiState())
    val uiState: LiveData<DashboardUiState> = _uiState

    fun loadDashboard(keypass: String?) {
        val trimmedKeypass = keypass?.trim()

        if (trimmedKeypass.isNullOrBlank()) {
            _uiState.value = DashboardUiState(
                isLoading = false,
                errorMessage = "Missing keypass. Please login again."
            )
            return
        }

        _uiState.value = DashboardUiState(isLoading = true)

        viewModelScope.launch {
            val result = dashboardRepository.getDashboard(trimmedKeypass)

            _uiState.value = result.fold(
                onSuccess = { response ->
                    DashboardUiState(
                        isLoading = false,
                        entities = response.entities,
                        entityTotal = response.entityTotal
                    )
                },
                onFailure = { error ->
                    DashboardUiState(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load dashboard data."
                    )
                }
            )
        }
    }
}