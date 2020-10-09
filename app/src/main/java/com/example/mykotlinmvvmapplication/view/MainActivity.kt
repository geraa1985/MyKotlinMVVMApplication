package com.example.mykotlinmvvmapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyApp.appGraph.inject(this)

        btn_hello.setOnClickListener {
            viewModel.onHelloButtonClick()
        }

        viewModel.getLiveData().observe(this) {
            Snackbar.make(btn_hello, it, Snackbar.LENGTH_SHORT).show()
        }

    }
}