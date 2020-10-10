package com.example.mykotlinmvvmapplication.data.network

import com.example.mykotlinmvvmapplication.data.repositoty.IGetData
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote

class APIAnswer: IGetData{

    override fun getNotes(): List<EntityNote> = listOf(
            EntityNote(
                    "Первая заметка",
                    "Текст первой заметки. Короткий, но важный",
                    0xfff06292.toInt()
            ),
            EntityNote(
                    "Вторая заметка",
                    "Текст второй заметки. Короткий, но важный",
                    0xff9575cd.toInt()
            ),
            EntityNote(
                    "Третья заметка",
                    "Текст третьей заметки. Короткий, но важный",
                    0xff64b5f6.toInt()
            ),
            EntityNote(
                    "Четвертая заметка",
                    "Текст четвертой заметки. Короткий, но важный",
                    0xff4db6ac.toInt()
            ),
            EntityNote(
                    "Пятая заметка",
                    "Текст пятой заметки. Короткий, но важный",
                    0xffb2ff59.toInt()
            ),
            EntityNote(
                    "Шестая заметка",
                    "Текст шестой заметки. Короткий, но важный",
                    0xffffeb3b.toInt()
            )
    )
}