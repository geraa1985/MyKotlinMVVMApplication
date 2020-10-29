package com.example.mykotlinmvvmapplication.di.koin

import com.example.mykotlinmvvmapplication.data.network.FireStoreAnswer
import com.example.mykotlinmvvmapplication.data.repositoty.IData
import com.example.mykotlinmvvmapplication.data.repositoty.Repository
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import com.example.mykotlinmvvmapplication.presentation.viewmodels.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreAnswer(get(), get()) } bind IData::class
    single { Repository(get()) } bind IRepository::class
    single { NotesInteractor(get())} bind INotesInteractor::class
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val logoutModule = module {
    viewModel { LogoutViewModel() }
}

val deleteModule = module {
    viewModel { DeleteNoteViewModel() }
}