package com.example.mykotlinmvvmapplication.di.components

import com.example.mykotlinmvvmapplication.di.modules.DataModule
import com.example.mykotlinmvvmapplication.presentation.ui.MainActivity
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppGraph {
    fun inject(mainActivity: MainActivity)
    fun inject(mainViewModel: MainViewModel)

}