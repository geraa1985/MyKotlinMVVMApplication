package com.example.mykotlinmvvmapplication.domain.usecases

import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

interface INotesInteractor {
    fun giveNotes(): List<EntityNote>
}