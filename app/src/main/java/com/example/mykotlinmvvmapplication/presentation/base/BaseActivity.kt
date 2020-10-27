package com.example.mykotlinmvvmapplication.presentation.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.firebase.ui.auth.AuthUI

abstract class BaseActivity<T> : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 777
    }

    abstract val viewModel: BaseViewModel<T>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }

        viewModel.apply {
            getSuccessLiveData().observe(this@BaseActivity, { value ->
                value ?: return@observe
                renderData(value)
            })
            getErrorLiveData().observe(this@BaseActivity, { error ->
                error?.let {
                    when (it) {
                        is NoteResult -> it.message?.let { errorText -> renderError(errorText) }
                        else -> startLoginActivity()
                    }
                } ?: return@observe
            })
        }
    }

    private fun renderError(errorText: String){
        Toast.makeText(this,errorText,Toast.LENGTH_SHORT).show()
    }

    abstract fun renderData(value: T)

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
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK){
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}