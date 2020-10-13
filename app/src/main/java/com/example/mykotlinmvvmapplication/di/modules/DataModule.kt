package com.example.mykotlinmvvmapplication.di.modules

import com.example.mykotlinmvvmapplication.data.local.DAOAnswer
import com.example.mykotlinmvvmapplication.data.network.APIAnswer
import com.example.mykotlinmvvmapplication.data.repositoty.Repository
import com.example.mykotlinmvvmapplication.domain.entities.EntityNotes
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import com.example.mykotlinmvvmapplication.presentation.adapters.NotesRVAdapter
import com.example.mykotlinmvvmapplication.presentation.statements.NotesStatement
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
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
    fun provideDAOAnswer() = DAOAnswer()

    @Provides
    @Singleton
    fun provideRepository(apiAnswer: APIAnswer,daoAnswer: DAOAnswer) = Repository(apiAnswer, daoAnswer)

    @Provides
    @Singleton
    fun provideEntityNotes(data: Repository) = EntityNotes(data)

    @Provides
    @Singleton
    fun provideNotesInteractor(entityNotes: EntityNotes) = NotesInteractor(entityNotes)

    @Provides
    @Singleton
    fun provideMainViewModel() = MainViewModel()

    @Provides
    @Singleton
    fun provideNotesStatement(interactor: NotesInteractor) = NotesStatement(interactor.giveNotes())

    @Provides
    @Singleton
    fun provideNotesRVAdapter() = NotesRVAdapter()

}