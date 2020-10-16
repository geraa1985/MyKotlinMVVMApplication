package com.example.mykotlinmvvmapplication.data.repositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

interface IData {

    fun getNotes(): LiveData<List<EntityNote>>

    fun updateNotes(entityNote: EntityNote)

}