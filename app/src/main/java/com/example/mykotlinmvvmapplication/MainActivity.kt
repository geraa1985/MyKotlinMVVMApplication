package com.example.mykotlinmvvmapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinmvvmapplication.di.AppGraph
import com.example.mykotlinmvvmapplication.di.DaggerAppGraph
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val appGraph: AppGraph = DaggerAppGraph.create()
    private val viewModel: MainViewModel = appGraph.mainVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_hello.setOnClickListener {
            viewModel.onHelloButtonClick()
        }

        viewModel.getLiveData().observe(this) {
            Snackbar.make(btn_hello, it, Snackbar.LENGTH_SHORT).show()
        }

    }
}