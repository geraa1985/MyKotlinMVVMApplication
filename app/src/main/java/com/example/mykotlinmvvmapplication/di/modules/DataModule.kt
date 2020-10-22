package com.example.mykotlinmvvmapplication.di.modules

import com.example.mykotlinmvvmapplication.data.network.FireStoreAnswer
import com.example.mykotlinmvvmapplication.data.repositoty.Repository
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideFSAnswer() = FireStoreAnswer()

    @Provides
    @Singleton
    fun provideRepository(fireStoreAnswer: FireStoreAnswer) = Repository(fireStoreAnswer)

    @Provides
    @Singleton
    fun provideNotesInteractor(data: Repository) = NotesInteractor(data)

}