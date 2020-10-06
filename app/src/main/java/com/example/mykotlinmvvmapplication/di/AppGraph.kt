package com.example.mykotlinmvvmapplication.di

import com.example.mykotlinmvvmapplication.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppGraph {
    fun mainVM(): MainViewModel
}