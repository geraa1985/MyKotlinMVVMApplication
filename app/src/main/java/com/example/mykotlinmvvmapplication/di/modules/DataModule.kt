package com.example.mykotlinmvvmapplication.di.modules

import com.example.mykotlinmvvmapplication.data.network.APIAnswer
import com.example.mykotlinmvvmapplication.data.repositoty.Repository
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import com.example.mykotlinmvvmapplication.presentation.viewmodels.NoteViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAPIAnswer() = APIAnswer()

    @Provides
    @Singleton
    fun provideRepository(apiAnswer: APIAnswer) = Repository(apiAnswer)

    @Provides
    @Singleton
    fun provideNotesInteractor(data: Repository) = NotesInteractor(data)

    @Provides
    @Singleton
    fun provideMainViewModel() = MainViewModel()

    @Provides
    fun provideNoteViewModel() = NoteViewModel()

}