package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import javax.inject.Inject

class MainViewModel : ViewModel() {

    private val successLiveData = MutableLiveData<List<Note>?>()
    private val errorLiveData = MutableLiveData<Throwable>()

    @Inject
    lateinit var interactor: NotesInteractor

    private val observer = Observer{ result: NoteResult? ->
        result?:return@Observer
        when (result) {
            is NoteResult.Success<*> -> successLiveData.value = result.data as List<Note>?
            is NoteResult.Error -> errorLiveData.value = result.error
        }
    }

    init {
        MyApp.appGraph.inject(this)
        interactor.giveNotes().observeForever(observer)
    }

    fun getSuccessLiveData():LiveData<List<Note>?> = successLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    override fun onCleared() {
        interactor.giveNotes().removeObserver(observer)
        super.onCleared()
    }
}