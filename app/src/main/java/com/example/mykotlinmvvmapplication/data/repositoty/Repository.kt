package com.example.mykotlinmvvmapplication.data.repositoty

import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository
import kotlinx.coroutines.channels.ReceiveChannel

class Repository(private val remoteData: IData) : IRepository {

    //Логика получения списка... либо с сервера, либо с локальной БД
    //А пока хардкод и неиспользование одного из источников...

    override fun getNotes(): ReceiveChannel<NoteResult> = remoteData.getNotes()

    override suspend fun saveNote(note: Note): Note? = remoteData.saveNote(note)

    override suspend fun getNoteById(id: String): Note? = remoteData.getNoteById(id)

    override suspend fun getUser(): User? = remoteData.getCurrentUser()

    override suspend fun deleteNoteById(id: String): Unit? = remoteData.deleteNoteById(id)
}