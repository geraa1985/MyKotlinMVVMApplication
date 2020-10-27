package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoAuthExceptions
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import javax.inject.Inject

class SplashViewModel : ViewModel() {

    private val successLiveData = MutableLiveData<Boolean?>()
    private val errorLiveData = MutableLiveData<Throwable>()

    @Inject
    lateinit var interactor: NotesInteractor

    init {
        MyApp.appGraph.inject(this)
    }

    private val observer = {user: User? ->
        successLiveData.value = user?.let { true }
        errorLiveData.value = NoAuthExceptions()
    }

    fun getSuccessLiveData(): LiveData<Boolean?> = successLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun requestUser() {
        interactor.giveUser().observeForever(observer)
    }

    override fun onCleared() {
        interactor.giveUser().removeObserver(observer)
        super.onCleared()
    }
}