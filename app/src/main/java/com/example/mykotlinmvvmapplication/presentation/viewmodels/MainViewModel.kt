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
    private val clickOnFabLiveData = MutableLiveData<Boolean>()
    private val clickOnLogoutLiveData = MutableLiveData<Boolean>()
    private val clickOnNoteLiveData = MutableLiveData<String>()

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
        interactor.getNotes().observeForever(observer)
    }

    fun getSuccessLiveData():LiveData<List<Note>?> = successLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    fun clickOnFab() {
        clickOnFabLiveData.value = true
    }

    fun getClickOnFabLiveData():LiveData<Boolean> = clickOnFabLiveData

    fun clickOnLogout() {
        clickOnLogoutLiveData.value = true
    }

    fun getClickOnLogoutLiveData():LiveData<Boolean> = clickOnLogoutLiveData

    fun clickOnNote(id:String) {
        clickOnNoteLiveData.value = id
    }

    fun getClickOnNoteLiveData():LiveData<String> = clickOnNoteLiveData

    override fun onCleared() {
        interactor.getNotes().removeObserver(observer)
        super.onCleared()
    }
}