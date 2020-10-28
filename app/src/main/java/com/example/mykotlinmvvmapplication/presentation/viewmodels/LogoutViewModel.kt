package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogoutViewModel:ViewModel() {

    private val clickOnYesLiveData = MutableLiveData<Boolean>()
    private val clickOnNoLiveData = MutableLiveData<Boolean>()

    fun clickOnYes(){
        clickOnYesLiveData.value = true
    }

    fun clickOnNo(){
        clickOnNoLiveData.value = true
    }

    fun getClickOnYesLiveData() = clickOnYesLiveData

    fun getClickOnNoLiveData() = clickOnNoLiveData
}