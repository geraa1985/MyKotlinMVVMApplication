package com.example.mykotlinmvvmapplication.domain.irepositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

interface IRepository {

    fun getNotes(): LiveData<List<EntityNote>>

    fun updateNotes(entityNote: EntityNote)

}