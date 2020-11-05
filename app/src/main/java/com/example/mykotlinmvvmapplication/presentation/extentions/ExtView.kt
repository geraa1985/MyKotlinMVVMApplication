package com.example.mykotlinmvvmapplication.presentation.extentions

import android.view.View

fun View.dip(value: Int): Int = context.dip(value)
fun View.dip(value: Float): Int = context.dip(value)