package com.example.mykotlinmvvmapplication.presentation.viewmodels

import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoAuthExceptions
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import com.example.mykotlinmvvmapplication.presentation.base.BaseViewModel
import javax.inject.Inject

class SplashViewModel : BaseViewModel<Boolean?>() {

    @Inject
    lateinit var interactor: NotesInteractor

    init {
        MyApp.appGraph.inject(this)
    }

    private val observer = {user: User? ->
        successLiveData.value = user?.let { true }
        errorLiveData.value = NoAuthExceptions()
    }

    fun requestUser() {
        interactor.giveUser().observeForever(observer)
    }

    override fun onCleared() {
        interactor.giveUser().removeObserver(observer)
        super.onCleared()
    }
}