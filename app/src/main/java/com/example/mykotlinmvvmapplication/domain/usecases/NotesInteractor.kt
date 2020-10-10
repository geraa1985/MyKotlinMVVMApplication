package com.example.mykotlinmvvmapplication.domain.usecases

import com.example.mykotlinmvvmapplication.domain.entities.EntityNote
import com.example.mykotlinmvvmapplication.domain.entities.IEntities

class NotesInteractor (entityNotes: IEntities): INotesInteractor {

    private val notes = entityNotes.getNotes()

    override fun giveNotes(): List<EntityNote> = notes

    //реализуем бизнес-логику, если она появится когда-то))

}