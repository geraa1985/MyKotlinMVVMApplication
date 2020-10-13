package com.example.mykotlinmvvmapplication.data.repositoty

import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

interface IGetData {

    fun getNotes(): List<EntityNote>

}