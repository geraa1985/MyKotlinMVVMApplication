package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import java.util.*
import javax.inject.Inject

class NoteViewModel : ViewModel() {

    private var pendingNote: Note? = null

    private val successLiveData = MutableLiveData<Note>()
    private val errorLiveData = MutableLiveData<Throwable>()

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

    fun getNoteById(id: String) {
        interactor.giveNoteById(id).observeForever{ result ->
            result?:return@observeForever
            when (result) {
                is NoteResult.Success<*> -> successLiveData.value = result.data as Note?
                is NoteResult.Error -> errorLiveData.value = result.error
            }
        }
    }

    fun getSuccessLiveData() = successLiveData
    fun getErrorLiveData() = errorLiveData



}