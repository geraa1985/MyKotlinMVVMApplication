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
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

class NotesRVAdapter(val viewModel: MainViewModel) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

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

    inner class ViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) {
            note.run {
                tv_title.text = title
                tv_title.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                tv_text.text = text
                (itemView as CardView).setCardBackgroundColor(ResourcesCompat.getColor(containerView.resources, getColor(), null))

                itemView.setOnClickListener {
                    viewModel.clickOnNote(id)
                }
            }
        }

    }
}