package com.vu.androidbasicapp.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vu.androidbasicapp.R
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import com.vu.androidbasicapp.ui.details.DetailsActivity
import org.json.JSONObject

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
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra(
                    DetailsActivity.EXTRA_ENTITY_JSON,
                    JSONObject(entity).toString()
                )
            }
            startActivity(intent)
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