package com.example.mykotlinmvvmapplication.di.modules

import com.example.mykotlinmvvmapplication.MainViewModel
import com.example.mykotlinmvvmapplication.Model
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideModel() = Model()


    @Provides
    @Singleton
    fun provideMainViewModel(model: Model) = MainViewModel(model)

}