package com.example.mykotlinmvvmapplication.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.base.BaseActivity
import com.example.mykotlinmvvmapplication.presentation.extentions.getColor
import com.example.mykotlinmvvmapplication.presentation.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?>() {

    companion object {
        private const val EXTRA_NOTE_ID = "noteId"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(EXTRA_NOTE_ID, noteId)
            context.startActivity(this)
        }
    }

    override val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    override val layoutRes: Int = R.layout.activity_note

    private var note: Note? = null
    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getStringExtra(EXTRA_NOTE_ID)

        noteId?.let {
            viewModel.getNoteById(it)
        } ?: run {
            setToolbar()
            initView()
        }

    }

    override fun renderError(errorText: String) {
        Snackbar.make(note_message, errorText, Snackbar.LENGTH_SHORT).show()
    }

    override fun renderData(value: Note?) {
        value?.let { note = value }
        setToolbar()
        initView()
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

        override fun afterTextChanged(s: Editable?) = viewModel.save(note_title.text.toString(), note_message.text.toString(), note)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        viewModel.updateNote()
        super.onBackPressed()
    }

}