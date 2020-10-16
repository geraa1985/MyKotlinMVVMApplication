package com.example.mykotlinmvvmapplication.presentation.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote
import com.example.mykotlinmvvmapplication.presentation.extentions.getColor
import kotlinx.android.synthetic.main.item_note.view.*

class NotesRVAdapter(val onClickListener: ((EntityNote) -> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var entityNotes: List<EntityNote> = listOf()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(entityNotes[position])

    override fun getItemCount() = entityNotes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entityNote: EntityNote) = with(itemView) {
            tv_title.text = entityNote.title
            tv_title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_text.text = entityNote.text
            (this as CardView).setCardBackgroundColor(ResourcesCompat.getColor(resources, entityNote.getColor(), null))

            itemView.setOnClickListener {
                onClickListener?.invoke(entityNote)
            }
        }
    }

}