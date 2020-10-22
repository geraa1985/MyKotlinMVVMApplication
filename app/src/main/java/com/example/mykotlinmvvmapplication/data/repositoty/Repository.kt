package com.example.mykotlinmvvmapplication.data.repositoty

import androidx.lifecycle.LiveData
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class Repository(private val remoteData: IData) : IRepository {

    //Логика получения списка... либо с сервера, либо с локальной БД
    //А пока хардкод и неиспользование одного из источников...

    override fun getNotes(): LiveData<NoteResult> = remoteData.getNotes()

    override fun saveNote(note: Note): LiveData<NoteResult> = remoteData.saveNote(note)

    override fun getNoteById(id: String): LiveData<NoteResult> = remoteData.getNoteById(id)

}