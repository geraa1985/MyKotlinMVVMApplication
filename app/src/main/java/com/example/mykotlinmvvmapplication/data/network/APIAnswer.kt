package com.example.mykotlinmvvmapplication.data.network

import androidx.lifecycle.MutableLiveData
import com.example.mykotlinmvvmapplication.data.repositoty.IData
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import java.util.*

class APIAnswer : IData {

    private var notesLiveData = MutableLiveData<List<Note>>()

    private val notes = mutableListOf(
            Note(
                    UUID.randomUUID().toString(),
                    "Первая заметка",
                    "Текст первой заметки. Короткий, но важный",
                    Color.GRAY
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Вторая заметка",
                    "Текст второй заметки. Короткий, но важный",
                    Color.BLUE
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Третья заметка",
                    "Текст третьей заметки. Короткий, но важный",
                    Color.YELLOW
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Четвертая заметка",
                    "Текст четвертой заметки. Короткий, но важный",
                    Color.RED
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Пятая заметка",
                    "Текст пятой заметки. Короткий, но важный",
                    Color.GREEN
            ),
            Note(
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

    override fun updateNotes(note: Note) {
        (addOrReplace(note))
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in notes.indices) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

}