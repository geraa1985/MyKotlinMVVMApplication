package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import java.util.*
import javax.inject.Inject

class NoteViewModel : ViewModel() {

    private val successLiveData = MutableLiveData<Note?>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val clickOnHomeLiveData = MutableLiveData<Boolean>()

    private var pendingNote: Note? = null

    @Inject
    lateinit var interactor: NotesInteractor

    init {
        MyApp.appGraph.inject(this)
    }

    fun save(title: String, message: String, note: Note?) {

        if (title.length < 3) return

        val newNote = note?.copy(
                title = title,
                text = message,
                lastChanged = Date()
        ) ?: Note(
                UUID.randomUUID().toString(),
                title = title,
                text = message,
                lastChanged = Date(),
                color = setColor()
        )

        pendingNote = newNote
    }

    private fun setColor(): Color {
        val list = Color.values()
        list.shuffle()
        return list.first()
    }

    fun updateNote() {
        pendingNote?.let {
            interactor.saveNote(it)
        }
    }

    private val observer = Observer { result: NoteResult ->
        when (result) {
            is NoteResult.Success<*> -> successLiveData.value = result.data as Note?
            is NoteResult.Error -> errorLiveData.value = result.error
        }
    }

    fun getSuccessLiveData(): LiveData<Note?> = successLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    private var id: String? = null

    fun getNoteById(id: String) {
        this.id = id
        interactor.getNoteById(id).observeForever(observer)
    }

    fun clickOnHome(){
        clickOnHomeLiveData.value = true
    }

    fun getClickOnHomeLiveData() = clickOnHomeLiveData

    override fun onCleared() {
        id?.let { interactor.getNoteById(it).removeObserver(observer) }
        super.onCleared()
    }
}