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
import com.example.mykotlinmvvmapplication.domain.entities.EntityNote
import com.example.mykotlinmvvmapplication.domain.entities.getColor
import com.example.mykotlinmvvmapplication.presentation.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_NOTE = "note"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, note: EntityNote? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(EXTRA_NOTE, note)
            context.startActivity(this)
        }
    }

    @Inject
    lateinit var noteViewModel: NoteViewModel

    private var note: EntityNote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MyApp.appGraph.inject(this)
        note = intent.getParcelableExtra(EXTRA_NOTE)

        setToolbar()
        initView()
    }

    private fun setToolbar() {
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.title_new_note)
    }

    private fun initView() {
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