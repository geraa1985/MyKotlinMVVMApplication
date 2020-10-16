package com.example.mykotlinmvvmapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

interface INotesInteractor {
    fun giveNotes(): LiveData<List<EntityNote>>
    fun updateNotes(entityNote: EntityNote)
}