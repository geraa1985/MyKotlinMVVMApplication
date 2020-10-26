package com.example.mykotlinmvvmapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class NotesInteractor (private val repository: IRepository): INotesInteractor {

    override fun giveNotes(): LiveData<NoteResult> = repository.getNotes()

    override fun giveNoteById(id: String): LiveData<NoteResult> = repository.getNoteById(id)

    override fun saveNote(note: Note): LiveData<NoteResult> = repository.saveNote(note)

    override fun giveUser(): LiveData<User?> = repository.getUser()

//реализуем бизнес-логику, если она появится когда-то))

}