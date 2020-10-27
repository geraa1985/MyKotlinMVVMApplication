package com.example.mykotlinmvvmapplication.presentation.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.presentation.viewmodels.SplashViewModel
import com.firebase.ui.auth.AuthUI

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 777
        fun start(context: Context) = Intent(context, SplashActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    private val viewModel = SplashViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.apply {
            getSuccessLiveData().observe(this@SplashActivity, { value ->
                value ?: return@observe
                renderData(value)
            })
            getErrorLiveData().observe(this@SplashActivity, { error ->
                error?.let {
                    startLoginActivity()
                } ?: return@observe
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    private fun renderData(value: Boolean?) {
        value?.takeIf { it }?.let {
            MainActivity.start(this)
            finish()
        }
    }

    private fun startLoginActivity() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.android_robot)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}