package com.example.mykotlinmvvmapplication.data.network

import androidx.lifecycle.MutableLiveData
import com.example.mykotlinmvvmapplication.data.repositoty.IData
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote
import java.util.*

class APIAnswer : IData {

    private var notesLiveData = MutableLiveData<List<EntityNote>>()

    private val notes = mutableListOf(
            EntityNote(
                    UUID.randomUUID().toString(),
                    "Первая заметка",
                    "Текст первой заметки. Короткий, но важный",
                    Color.GRAY
            ),
            EntityNote(
                    UUID.randomUUID().toString(),
                    "Вторая заметка",
                    "Текст второй заметки. Короткий, но важный",
                    Color.BLUE
            ),
            EntityNote(
                    UUID.randomUUID().toString(),
                    "Третья заметка",
                    "Текст третьей заметки. Короткий, но важный",
                    Color.YELLOW
            ),
            EntityNote(
                    UUID.randomUUID().toString(),
                    "Четвертая заметка",
                    "Текст четвертой заметки. Короткий, но важный",
                    Color.RED
            ),
            EntityNote(
                    UUID.randomUUID().toString(),
                    "Пятая заметка",
                    "Текст пятой заметки. Короткий, но важный",
                    Color.GREEN
            ),
            EntityNote(
                    UUID.randomUUID().toString(),
                    "Шестая заметка",
                    "Текст шестой заметки. Короткий, но важный",
                    Color.VIOLET
            )
    )

    init {
        notesLiveData.value = notes
    }

    override fun getNotes() = notesLiveData

    override fun updateNotes(entityNote: EntityNote) {
        (addOrReplace(entityNote))
        notesLiveData.value = notes
    }

    private fun addOrReplace(entityNote: EntityNote) {
        for (i in notes.indices) {
            if (notes[i] == entityNote) {
                notes[i] = entityNote
                return
            }
        }
        notes.add(entityNote)
    }

}