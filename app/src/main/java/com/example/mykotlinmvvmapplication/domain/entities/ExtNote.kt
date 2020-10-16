package com.example.mykotlinmvvmapplication.domain.entities

import com.example.mykotlinmvvmapplication.R

fun EntityNote.getColor(): Int {
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