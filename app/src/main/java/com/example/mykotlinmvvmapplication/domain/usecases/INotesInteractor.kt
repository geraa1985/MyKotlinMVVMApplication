package com.example.mykotlinmvvmapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User

interface INotesInteractor {
    fun getNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getUser(): LiveData<User?>
}