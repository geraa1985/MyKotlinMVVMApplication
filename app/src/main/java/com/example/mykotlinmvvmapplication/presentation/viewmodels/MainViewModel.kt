package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.repositoty.Repository
import com.example.mykotlinmvvmapplication.domain.entities.Note
import javax.inject.Inject

class MainViewModel : ViewModel() {

    private val liveData = MutableLiveData<List<Note>>()

    @Inject
    lateinit var noteRepository: Repository

    init {
        MyApp.appGraph.inject(this)
        noteRepository.getNotes().observeForever {
            liveData.value = it
        }
    }

    fun getLiveData() = liveData

}