package com.example.mykotlinmvvmapplication.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.mykotlinmvvmapplication.MyApp
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.extentions.getColor
import com.example.mykotlinmvvmapplication.presentation.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_NOTE_ID = "noteId"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(EXTRA_NOTE_ID, noteId)
            context.startActivity(this)
        }
    }

    @Inject
    lateinit var noteViewModel: NoteViewModel

    private var note: Note? = null
    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MyApp.appGraph.inject(this)

        noteId = intent.getStringExtra(EXTRA_NOTE_ID)

        noteId?.let {
            noteViewModel.getNoteById(it)
        } ?: run {
            setToolbar()
            initView()
        }

        noteViewModel.apply {
            getSuccessLiveData().observe(this@NoteActivity, { result ->
                note = result
                setToolbar()
                initView()
            })
            getErrorLiveData().observe(this@NoteActivity, { error ->
                error.message?.let { errorText -> Snackbar.make(note_message, errorText, Snackbar.LENGTH_SHORT).show() }
            })
        }
    }


    private fun setToolbar() {
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.title_new_note)
    }

    private fun initView() {
        note_title.removeTextChangedListener(textChangeListener)
        note_message.removeTextChangedListener(textChangeListener)
        note?.let {
            note_title.setText(it.title)
            note_message.setText(it.text)
            toolbar.setBackgroundColor(ResourcesCompat.getColor(resources, it.getColor(), null))
        }
        note_title.addTextChangedListener(textChangeListener)
        note_message.addTextChangedListener(textChangeListener)
    }

    private val textChangeListener = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) = noteViewModel.save(note_title.text.toString(), note_message.text.toString(), note)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        noteViewModel.updateNote()
        super.onBackPressed()
    }
}