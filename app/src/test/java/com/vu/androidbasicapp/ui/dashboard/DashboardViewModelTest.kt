package com.vu.androidbasicapp.ui.dashboard

import com.vu.androidbasicapp.MainDispatcherRule
import com.vu.androidbasicapp.data.model.DashboardResponse
import com.vu.androidbasicapp.data.model.Entity
import com.vu.androidbasicapp.data.repository.DashboardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        mainDispatcherRule.setUp()
    }

    @After
    fun tearDown() {
        mainDispatcherRule.tearDown()
    }

    private class FakeDashboardRepository(
        private val result: Result<DashboardResponse>
    ) : DashboardRepository {

        var capturedKeypass: String? = null

        override suspend fun getDashboard(
            keypass: String
        ): Result<DashboardResponse> {
            capturedKeypass = keypass
            return result
        }
    }

    @Test
    fun loadDashboard_withMissingKeypass_setsErrorMessage() {
        val repository = FakeDashboardRepository(
            Result.success(
                DashboardResponse(
                    entities = emptyList(),
                    entityTotal = 0
                )
            )
        )
        val viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard(" ")

        val state = viewModel.uiState.value

        assertFalse(state!!.isLoading)
        assertEquals("Missing keypass. Please login again.", state.errorMessage)
        assertTrue(state.entities.isEmpty())
        assertEquals(0, state.entityTotal)
    }

    @Test
    fun loadDashboard_withValidKeypass_loadsEntities() {
        val entity: Entity = mapOf(
            "courseName" to "Android Application Development",
            "courseCode" to "NIT3213",
            "description" to "Final assignment entity description"
        )

        val repository = FakeDashboardRepository(
            Result.success(
                DashboardResponse(
                    entities = listOf(entity),
                    entityTotal = 1
                )
            )
        )
        val viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard(" courses ")

        val state = viewModel.uiState.value

        assertEquals("courses", repository.capturedKeypass)
        assertFalse(state!!.isLoading)
        assertEquals(null, state.errorMessage)
        assertEquals(1, state.entityTotal)
        assertEquals(1, state.entities.size)
        assertEquals("Android Application Development", state.entities[0]["courseName"])
        assertEquals("NIT3213", state.entities[0]["courseCode"])
    }

    @Test
    fun loadDashboard_whenRepositoryFails_setsErrorMessage() {
        val repository = FakeDashboardRepository(
            Result.failure(Exception("Failed to load dashboard data."))
        )
        val viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard("courses")

        val state = viewModel.uiState.value

        assertFalse(state!!.isLoading)
        assertEquals("Failed to load dashboard data.", state.errorMessage)
        assertTrue(state.entities.isEmpty())
        assertEquals(0, state.entityTotal)
    }
}
