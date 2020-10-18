package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.data.repositoty.Repository
import com.example.mykotlinmvvmapplication.domain.entities.Note
import javax.inject.Inject

class MainViewModel : ViewModel() {

    private val successLiveData = MutableLiveData<List<Note>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    @Inject
    lateinit var noteRepository: Repository

    init {
        MyApp.appGraph.inject(this)
        noteRepository.getNotes().observeForever {
            it ?: return@observeForever
            when (it) {
                is NoteResult.Success<*> -> successLiveData.value = it.data as List<Note>?
                is NoteResult.Error -> errorLiveData.value = it.error
            }
        }
    }

    fun getSuccessLiveData() = successLiveData
    fun getErrorLiveData() = errorLiveData

}