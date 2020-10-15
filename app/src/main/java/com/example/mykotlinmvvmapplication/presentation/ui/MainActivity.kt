package com.example.mykotlinmvvmapplication.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.presentation.adapters.NotesRVAdapter
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyApp.appGraph.inject(this)
        adapter = NotesRVAdapter { NoteActivity.start(this, it) }

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        rv_notes.adapter = adapter

        mainViewModel.getLiveData().observe(this, { value ->
            value?.let { adapter.entityNotes = it }
        })

        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }
}