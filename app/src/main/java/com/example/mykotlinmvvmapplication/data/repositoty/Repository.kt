package com.example.mykotlinmvvmapplication.data.repositoty

import com.example.mykotlinmvvmapplication.domain.entities.EntityNote
import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class Repository(private val remoteData: IGetData, private val localData: IGetData) : IRepository {

    //Логика получения списка... либо с сервера, либо с локальной БД
    //А пока хардкод и неиспользование одного из источников...
    override fun getNotes(): List<EntityNote> = remoteData.getNotes()
}