package com.example.mykotlinmvvmapplication.di.components

import com.example.mykotlinmvvmapplication.MainActivity
import com.example.mykotlinmvvmapplication.di.modules.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [DataModule::class])
interface AppGraph {
    fun inject(mainActivity: MainActivity)
}