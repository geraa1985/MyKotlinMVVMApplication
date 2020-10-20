package com.example.mykotlinmvvmapplication.presentation.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.extentions.getColor
import kotlinx.android.synthetic.main.item_note.view.*

class NotesRVAdapter(val onClickListener: ((id: String) -> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_note,
                            parent,
                            false
                    )
            )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    override fun getItemCount() = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = itemView.run {
            note.run {
                tv_title.text = title
                tv_title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                tv_text.text = text
                (itemView as CardView).setCardBackgroundColor(ResourcesCompat.getColor(resources, getColor(), null))

                itemView.setOnClickListener {
                    onClickListener?.invoke(id)
                }
            }

        }
    }

}