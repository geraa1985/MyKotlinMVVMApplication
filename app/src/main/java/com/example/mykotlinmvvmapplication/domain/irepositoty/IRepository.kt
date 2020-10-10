package com.example.mykotlinmvvmapplication.domain.irepositoty

import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

interface IRepository {

    fun getNotes(): List<EntityNote>

}