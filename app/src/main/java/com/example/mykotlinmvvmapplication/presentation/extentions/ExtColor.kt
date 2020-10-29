package com.example.mykotlinmvvmapplication.presentation.extentions

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Color

fun Color.getColor(): Int {
    return when (this) {
        Color.GRAY -> R.color.gray
        Color.YELLOW -> R.color.yellow
        Color.GREEN -> R.color.green
        Color.BLUE -> R.color.blue
        Color.RED -> R.color.red
        Color.VIOLET -> R.color.violet
        Color.WHITE -> R.color.white
    }
}

fun Color.getColorInt(context: Context) = ResourcesCompat.getColor(context.resources, getColor(), null)
