package com.example.mykotlinmvvmapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.Note

interface INotesInteractor {
    fun giveNotes(): LiveData<List<Note>>
    fun updateNotes(note: Note)
}