package com.example.mykotlinmvvmapplication.domain.usecases

import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository
import kotlinx.coroutines.channels.ReceiveChannel

class NotesInteractor (private val repository: IRepository): INotesInteractor {

    override fun getNotes(): ReceiveChannel<NoteResult> = repository.getNotes()

    override suspend fun getNoteById(id: String): Note? = repository.getNoteById(id)

    override suspend fun saveNote(note: Note): Note? = repository.saveNote(note)

    override suspend fun getUser(): User? = repository.getUser()

    override suspend fun deleteNoteById(id: String): Unit? = repository.deleteNoteById(id)

    //реализуем бизнес-логику, если она появится когда-то))

}