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

    private var noteLiveData: LiveData<NoteResult>? = null
    private var deleteLiveData: LiveData<NoteResult>? = null

    private val successLiveData = MutableLiveData<Note?>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val clickOnHomeLiveData = MutableLiveData<Boolean>()
    private val clickOnColorLiveData = MutableLiveData<Boolean>()
    private val clickOnDeleteLiveData = MutableLiveData<Boolean>()
    private val successDeleteLiveData = MutableLiveData<Boolean>()

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

    private val noteObserver = Observer { result: NoteResult ->
        when (result) {
            is NoteResult.Success<*> -> successLiveData.value = result.data as Note?
            is NoteResult.Error -> errorLiveData.value = result.error
        }
    }

    private val deleteObserver = Observer { result: NoteResult ->
        when (result) {
            is NoteResult.Success<*> -> successDeleteLiveData.value = true
            is NoteResult.Error -> errorLiveData.value = result.error
        }
    }

    fun getSuccessLiveData(): LiveData<Note?> = successLiveData

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData

    private var id: String? = null

    fun getNoteById(id: String) {
        this.id = id
        noteLiveData = interactor.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }

    fun clickOnHome() {
        clickOnHomeLiveData.value = true
    }

    fun getClickOnHomeLiveData() = clickOnHomeLiveData

    fun clickOnColor() {
        clickOnColorLiveData.value = true
    }

//    fun getClickOnColorLiveData() = clickOnColorLiveData

    fun clickOnDelete() {
        clickOnDeleteLiveData.value = true
    }

    fun getClickOnDeleteLiveData() = clickOnDeleteLiveData

    fun confirmedDelete(id: String?) {
        id?.let {
            deleteLiveData = interactor.deleteNoteById(id)
            deleteLiveData?.observeForever(deleteObserver)
        }
    }

    fun getSuccessDeleteLiveData() = successDeleteLiveData

    override fun onCleared() {
        noteLiveData?.removeObserver(noteObserver)
//        deleteLiveData?.removeObserver(deleteObserver)
        super.onCleared()
    }
}