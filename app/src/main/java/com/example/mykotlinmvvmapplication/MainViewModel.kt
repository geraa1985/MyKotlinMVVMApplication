package com.example.mykotlinmvvmapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(private val model: Model): ViewModel() {

    private val liveData = MutableLiveData<String>()

    fun onHelloButtonClick() {
        val data = model.getdata()
        liveData.value = data
    }

    fun getLiveData() = liveData

}