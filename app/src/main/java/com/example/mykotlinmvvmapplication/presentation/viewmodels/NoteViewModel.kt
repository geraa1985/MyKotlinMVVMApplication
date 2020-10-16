package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.NotesInteractor
import java.util.*
import javax.inject.Inject

class NoteViewModel : ViewModel() {

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
            interactor.updateNotes(it)
        }
    }

}