package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.presentation.statements.NotesStatement
import javax.inject.Inject

class MainViewModel : ViewModel() {

    private val liveData = MutableLiveData<NotesStatement>()

    @Inject
    lateinit var notesStatement: NotesStatement

    init {
        MyApp.appGraph.inject(this)
        liveData.value = notesStatement
    }

    fun getLiveData() = liveData

}