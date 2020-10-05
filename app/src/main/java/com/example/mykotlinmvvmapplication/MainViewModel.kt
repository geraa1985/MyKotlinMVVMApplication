package com.example.mykotlinmvvmapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val model = Model()
    private val liveData = MutableLiveData<String>()

    fun onHelloButtonClick() {
        val data = model.getdata()
        liveData.value = data
    }

    fun getLiveData() = liveData

}