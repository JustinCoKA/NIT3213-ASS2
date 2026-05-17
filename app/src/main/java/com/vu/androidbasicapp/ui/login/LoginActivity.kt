package com.vu.androidbasicapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vu.androidbasicapp.R
import com.vu.androidbasicapp.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvLoginError: TextView
    private lateinit var progressLogin: ProgressBar

    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvLoginError = findViewById(R.id.tvLoginError)
        progressLogin = findViewById(R.id.progressLogin)

        btnLogin.setOnClickListener {
            viewModel.login(
                username = etUsername.text.toString(),
                password = etPassword.text.toString()
            )
        }

        observeLoginState()
    }

    private fun observeLoginState() {
        viewModel.uiState.observe(this) { state ->
            progressLogin.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            btnLogin.isEnabled = !state.isLoading

            if (state.errorMessage != null) {
                tvLoginError.visibility = View.VISIBLE
                tvLoginError.text = state.errorMessage
            } else {
                tvLoginError.visibility = View.GONE
            }

            if (state.keypass != null && !hasNavigated) {
                hasNavigated = true

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("KEYPASS", state.keypass)
                startActivity(intent)

                viewModel.clearNavigationState()
            }
        }
    }
}