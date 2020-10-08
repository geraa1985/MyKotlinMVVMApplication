package com.example.mykotlinmvvmapplication.di

import com.example.mykotlinmvvmapplication.di.components.AppGraph
import com.example.mykotlinmvvmapplication.di.components.DaggerAppGraph

object DaggerGraph {
    val appGraph: AppGraph = DaggerAppGraph.create()
}