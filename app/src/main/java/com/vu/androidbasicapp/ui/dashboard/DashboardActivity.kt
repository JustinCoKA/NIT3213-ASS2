package com.vu.androidbasicapp.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vu.androidbasicapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: DashboardViewModel

    private lateinit var rvEntities: RecyclerView
    private lateinit var tvDashboardTitle: TextView
    private lateinit var tvEntityTotal: TextView
    private lateinit var tvDashboardError: TextView
    private lateinit var progressDashboard: ProgressBar

    private lateinit var entityAdapter: EntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        rvEntities = findViewById(R.id.rvEntities)
        tvDashboardTitle = findViewById(R.id.tvDashboardTitle)
        tvEntityTotal = findViewById(R.id.tvEntityTotal)
        tvDashboardError = findViewById(R.id.tvDashboardError)
        progressDashboard = findViewById(R.id.progressDashboard)

        setupRecyclerView()
        observeDashboardState()

        val keypass = intent.getStringExtra("KEYPASS")
        viewModel.loadDashboard(keypass)
    }

    private fun setupRecyclerView() {
        entityAdapter = EntityAdapter { entity ->
            // Step 11에서 DetailsActivity 이동 코드로 교체
            Toast.makeText(
                this,
                "Details screen will be implemented in Step 11.",
                Toast.LENGTH_SHORT
            ).show()
        }

        rvEntities.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = entityAdapter
        }
    }

    private fun observeDashboardState() {
        viewModel.uiState.observe(this) { state ->
            progressDashboard.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            if (state.errorMessage != null) {
                tvDashboardError.visibility = View.VISIBLE
                tvDashboardError.text = state.errorMessage
            } else {
                tvDashboardError.visibility = View.GONE
            }

            tvEntityTotal.text = "Total entities: ${state.entityTotal}"
            entityAdapter.submitList(state.entities)
        }
    }
}