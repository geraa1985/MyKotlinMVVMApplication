package com.example.mykotlinmvvmapplication.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.adapters.NotesRVAdapter
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel.apply {
            getSuccessLiveData().observe(this@MainActivity, { value ->
                value?.let {
                    renderData(value)
                } ?: return@observe
            })
            getErrorLiveData().observe(this@MainActivity, { error ->
                error?.let {
                    it.message?.let { errorText -> renderError(errorText) }
                } ?: return@observe
            })
            getClickOnFabLiveData().observe(this@MainActivity) {
                it.let { NoteActivity.start(this@MainActivity) }
            }
            getClickOnLogoutLiveData().observe(this@MainActivity) {
                it.let { LogoutDialogFragment().show(supportFragmentManager, "LOGOUT") }
            }
            getClickOnNoteLiveData().observe(this@MainActivity) {
                NoteActivity.start(this@MainActivity, it)
            }
        }

        adapter = NotesRVAdapter(viewModel)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        rv_notes.adapter = adapter

        fab.setOnClickListener {
            viewModel.clickOnFab()
        }
    }

    private fun renderData(value: List<Note>?) {
        value?.let { adapter.notes = value }
    }

    private fun renderError(errorText: String) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            MenuInflater(this).inflate(R.menu.main_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.logout -> viewModel.clickOnLogout().let { true }
                else -> false
            }

}