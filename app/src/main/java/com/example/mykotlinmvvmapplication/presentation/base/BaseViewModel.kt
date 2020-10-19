package com.example.mykotlinmvvmapplication.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {

    open val successLiveData = MutableLiveData<T>()
    open val errorLiveData = MutableLiveData<Throwable>()

    open fun getSuccessLiveData():LiveData<T> = successLiveData
    open fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
}