package com.example.mykotlinmvvmapplication.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinmvvmapplication.presentation.base.BaseActivity
import com.example.mykotlinmvvmapplication.presentation.viewmodels.SplashViewModel

class SplashActivity: BaseActivity<Boolean?>() {

    companion object {
        fun start(context: Context) = Intent(context, SplashActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel by lazy { ViewModelProvider(this).get(SplashViewModel::class.java) }
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    override fun renderData(value: Boolean?) {
        value?.takeIf { it }?.let {
            MainActivity.start(this)
            finish()
        }
    }

}