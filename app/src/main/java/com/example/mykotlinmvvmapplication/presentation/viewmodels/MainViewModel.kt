package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.data.local.model.IModel

class MainViewModel constructor(private val model: IModel) : ViewModel() {

    private val liveData = MutableLiveData<String>()

    fun onHelloButtonClick() {
        val data = model.getdata()
        liveData.value = data
    }

    fun getLiveData() = liveData

}