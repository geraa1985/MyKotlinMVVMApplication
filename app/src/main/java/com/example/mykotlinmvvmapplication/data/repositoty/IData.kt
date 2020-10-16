package com.example.mykotlinmvvmapplication.data.repositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.Note

interface IData {

    fun getNotes(): LiveData<List<Note>>

    fun updateNotes(note: Note)

}