package com.example.mykotlinmvvmapplication.data.repositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class Repository(private val remoteData: IData, private val localData: IData) : IRepository {

    //Логика получения списка... либо с сервера, либо с локальной БД
    //А пока хардкод и неиспользование одного из источников...

    override fun getNotes(): LiveData<List<Note>> = remoteData.getNotes()

    override fun updateNotes(note: Note) {
        remoteData.updateNotes(note)
        localData.updateNotes(note)
    }


}