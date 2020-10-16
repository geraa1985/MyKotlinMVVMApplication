package com.example.mykotlinmvvmapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class NotesInteractor (private val data: IRepository): INotesInteractor {

    private val notes = data.getNotes()

    override fun giveNotes(): LiveData<List<Note>> = notes

    override fun updateNotes(note: Note) {
        data.updateNotes(note)
    }


    //реализуем бизнес-логику, если она появится когда-то))

}