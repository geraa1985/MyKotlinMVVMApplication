package com.example.mykotlinmvvmapplication

import android.app.Application
import com.example.mykotlinmvvmapplication.di.components.AppGraph
import com.example.mykotlinmvvmapplication.di.components.DaggerAppGraph

class MyApp:Application() {

    companion object {
        private var instance: MyApp? = null
        val appGraph: AppGraph = DaggerAppGraph.create()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}