package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.Observer
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import com.example.mykotlinmvvmapplication.presentation.base.BaseViewModel
import java.util.*
import javax.inject.Inject

class NoteViewModel : BaseViewModel<Note?>() {

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

    private val observer = Observer { result: NoteResult? ->
        result?:return@Observer
        when (result) {
            is NoteResult.Success<*> -> successLiveData.value = result.data as Note?
            is NoteResult.Error -> errorLiveData.value = result.error
        }
    }

    private var id: String? = null

    fun getNoteById(id: String) {
        this.id = id
        interactor.giveNoteById(id).observeForever(observer)
    }

    override fun onCleared() {
        id?.let { interactor.giveNoteById(it).removeObserver(observer) }
        super.onCleared()
    }
}