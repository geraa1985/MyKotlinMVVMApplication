package com.example.mykotlinmvvmapplication.domain.irepositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User

interface IRepository {

    fun getNotes(): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun getUser(): LiveData<User?>
    fun deleteNoteById(id: String): LiveData<NoteResult>

}