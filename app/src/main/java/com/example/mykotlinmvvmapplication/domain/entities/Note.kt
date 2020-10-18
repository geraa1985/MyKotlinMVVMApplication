package com.example.mykotlinmvvmapplication.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(
        val id: String = "",
        val title: String = "",
        val text: String = "",
        val color: Color = Color.YELLOW,
        val lastChanged: Date = Date()
) :  Parcelable{

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (javaClass != other?.javaClass) return false
        other as Note
        if (other.id != id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}