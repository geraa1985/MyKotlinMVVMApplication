package com.example.mykotlinmvvmapplication.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        viewModel.apply {
            getSuccessLiveData().observe(this@BaseActivity, { value ->
                value ?: return@observe
                renderData(value)
            })
            getErrorLiveData().observe(this@BaseActivity, { error ->
                error ?: return@observe
                error.message?.let { errorText -> renderError(errorText) }
            })
        }
    }

    abstract fun renderError(errorText: String)

    abstract fun renderData(value: T)

}