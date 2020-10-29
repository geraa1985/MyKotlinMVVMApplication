package com.example.mykotlinmvvmapplication.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.extentions.getColor
import com.example.mykotlinmvvmapplication.presentation.extentions.getColorInt
import com.example.mykotlinmvvmapplication.presentation.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_NOTE_ID = "noteId"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(EXTRA_NOTE_ID, noteId)
            context.startActivity(this)
        }
    }

    val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private var note: Note? = null
    private var noteId: String? = null
    private lateinit var color: Color

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        color = viewModel.setColor()

        viewModel.apply {
            getSuccessLiveData().observe(this@NoteActivity, { value ->
                value?.let {
                    renderData(value)
                } ?: return@observe
            })
            getErrorLiveData().observe(this@NoteActivity, { error ->
                error?.let {
                    it.message?.let { errorText -> renderError(errorText) }
                } ?: return@observe
            })
            getClickOnHomeLiveData().observe(this@NoteActivity) {
                onBackPressed()
            }
            getClickOnDeleteLiveData().observe(this@NoteActivity) {
                DeleteNoteDialogFragment().apply {
                    show(supportFragmentManager, "DELETE")
                    getConfirmToDeleteLiveData().observe(this@NoteActivity) {
                        viewModel.confirmedDelete(noteId)
                    }
                }
            }
            getSuccessDeleteLiveData().observe(this@NoteActivity) {
                finish()
            }
            getClickOnColorLiveData().observe(this@NoteActivity) {
                if (colorPicker.isOpen) {
                    colorPicker.close()
                } else {
                    colorPicker.open()
                }
            }
        }

        noteId = intent.getStringExtra(EXTRA_NOTE_ID)
        noteId?.let {
            viewModel.getNoteById(it)
        } ?: run {
            setToolbar()
            initView()
        }
    }

    private fun renderData(value: Note?) {
        value?.let { note = value }
        setToolbar()
        initView()
    }

    private fun renderError(errorText: String) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
    }

    private fun setToolbar() {
        note?.let {
            supportActionBar?.title = it.lastChanged.let {date ->
                SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(date)
            } ?: getString(R.string.title_new_note)
            color = it.color
        }
    }

    private fun initView() {
        note?.let {
            note_title.setText(it.title)
            note_message.setText(it.text)
            toolbar.setBackgroundColor(ResourcesCompat.getColor(resources, it.getColor(), null))
        }

        colorPicker.onColorClickListener = {
            toolbar.setBackgroundColor(it.getColorInt(this))
            color = it
            viewModel.save(note_title.text.toString(), note_message.text.toString(), color, note)
        }

        note_title.addTextChangedListener(textChangeListener)
        note_message.addTextChangedListener(textChangeListener)
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) = viewModel.save(note_title.text.toString(), note_message.text.toString(), color, note)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> viewModel.clickOnHome().let { true }
                R.id.note_color -> viewModel.clickOnColor().let { true }
                R.id.note_delete -> viewModel.clickOnDelete().let { true }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onBackPressed() {
        viewModel.updateNote()
        super.onBackPressed()
    }

    override fun onDestroy() {
        note_title.removeTextChangedListener(textChangeListener)
        note_message.removeTextChangedListener(textChangeListener)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return menuInflater.inflate(R.menu.note_menu, menu).let { true }
    }

}