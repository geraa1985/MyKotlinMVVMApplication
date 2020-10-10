package com.example.mykotlinmvvmapplication.domain.entities

import com.example.mykotlinmvvmapplication.domain.irepositoty.IRepository

class EntityNotes(data: IRepository): IEntities {

    private val notes = data.getNotes()

    override fun getNotes() = notes

    //можно как-то модифицировать список или привинтить какие-то бизнес-фичи перед передачей интерактеру
}