package com.example.mykotlinmvvmapplication.di.components

import com.example.mykotlinmvvmapplication.di.modules.DataModule
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import com.example.mykotlinmvvmapplication.presentation.viewmodels.NoteViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppGraph {

    fun inject(mainViewModel: MainViewModel)

    fun inject(noteViewModel: NoteViewModel)

}