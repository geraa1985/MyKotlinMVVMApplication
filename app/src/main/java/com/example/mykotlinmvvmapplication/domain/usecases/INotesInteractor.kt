package com.example.mykotlinmvvmapplication.domain.usecases

import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import kotlinx.coroutines.channels.ReceiveChannel

interface INotesInteractor {
    fun getNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note?
    suspend fun saveNote(note: Note): Note?
    suspend fun getUser(): User?
    suspend fun deleteNoteById(id: String): Unit?

}