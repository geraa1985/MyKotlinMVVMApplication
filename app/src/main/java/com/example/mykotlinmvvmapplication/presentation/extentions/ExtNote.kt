package com.example.mykotlinmvvmapplication.presentation.extentions

import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note

fun Note.getColor(): Int {
    return when (color) {
        Color.GRAY -> R.color.gray
        Color.YELLOW -> R.color.yellow
        Color.GREEN -> R.color.green
        Color.BLUE -> R.color.blue
        Color.RED -> R.color.red
        Color.VIOLET -> R.color.violet
        Color.WHITE -> R.color.white
    }
}