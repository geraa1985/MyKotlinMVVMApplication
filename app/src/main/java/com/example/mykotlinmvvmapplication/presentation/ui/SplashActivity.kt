package com.example.mykotlinmvvmapplication.presentation.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.presentation.viewmodels.SplashViewModel
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        private const val RC_SIGN_IN = 777

        fun start(context: Context) = Intent(context, SplashActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + Job() }
    private lateinit var job: Job

    private val viewModel: SplashViewModel by viewModel()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.apply {
            job = launch {
                getSuccessChannel().consumeEach {
                    it?.let {
                        renderData(true)
                    }
                }
                getErrorChannel().consumeEach {
                    startLoginActivity()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    @ExperimentalCoroutinesApi
    private fun renderData(value: Boolean?) {
        value?.takeIf { it }?.let {
            MainActivity.start(this)
            this@SplashActivity.finish()
        }
    }

    private fun startLoginActivity() {
        val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

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