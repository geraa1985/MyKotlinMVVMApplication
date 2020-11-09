package com.example.mykotlinmvvmapplication.domain.irepositoty

import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import kotlinx.coroutines.channels.ReceiveChannel

interface IRepository {

    fun getNotes(): ReceiveChannel<NoteResult>
    suspend fun saveNote(note: Note): Note?
    suspend fun getNoteById(id: String): Note?
    suspend fun getUser(): User?
    suspend fun deleteNoteById(id: String): Unit?

}