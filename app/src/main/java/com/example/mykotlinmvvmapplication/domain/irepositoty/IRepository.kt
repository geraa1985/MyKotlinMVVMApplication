package com.example.mykotlinmvvmapplication.domain.irepositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.Note

interface IRepository {

    fun getNotes(): LiveData<List<Note>>

    fun updateNotes(note: Note)

}