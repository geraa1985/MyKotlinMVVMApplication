package com.example.mykotlinmvvmapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class NotesInteractor (private val data: IRepository): INotesInteractor {

    private val notes = data.getNotes()

    override fun giveNotes(): LiveData<List<EntityNote>> = notes

    override fun updateNotes(entityNote: EntityNote) {
        data.updateNotes(entityNote)
    }


    //реализуем бизнес-логику, если она появится когда-то))

}