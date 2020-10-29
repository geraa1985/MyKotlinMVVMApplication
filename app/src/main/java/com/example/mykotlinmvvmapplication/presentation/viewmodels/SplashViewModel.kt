package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.data.network.NoAuthExceptions
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor

class SplashViewModel(private val interactor: INotesInteractor) : ViewModel() {

    private val successLiveData = MutableLiveData<Boolean?>()
    private val errorLiveData = MutableLiveData<Throwable>()

    private val observer = {user: User? ->
        successLiveData.value = user?.let { true }
        errorLiveData.value = NoAuthExceptions()
    }

    fun getSuccessLiveData(): LiveData<Boolean?> = successLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun requestUser() {
        interactor.getUser().observeForever(observer)
    }

    override fun onCleared() {
        interactor.getUser().removeObserver(observer)
        super.onCleared()
    }
}