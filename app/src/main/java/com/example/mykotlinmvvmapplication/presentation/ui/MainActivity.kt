package com.example.mykotlinmvvmapplication.presentation.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.adapters.NotesRVAdapter
import com.example.mykotlinmvvmapplication.presentation.base.BaseActivity
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override val layoutRes = R.layout.activity_main

    private lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = NotesRVAdapter { NoteActivity.start(this, it) }

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        rv_notes.adapter = adapter

        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    override fun renderData(value: List<Note>?) {
        value?.let { adapter.notes = value }
    }

    override fun renderError(errorText: String) {
        Snackbar.make(rv_notes, errorText, Snackbar.LENGTH_SHORT).show()
    }

}