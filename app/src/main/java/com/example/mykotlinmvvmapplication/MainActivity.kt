package com.example.mykotlinmvvmapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        btn_hello.setOnClickListener {
            viewModel.onHelloButtonClick()
        }

        viewModel.getLiveData().observe(this, {
            Snackbar.make(btn_hello, it, Snackbar.LENGTH_SHORT).show()
        })

    }
}