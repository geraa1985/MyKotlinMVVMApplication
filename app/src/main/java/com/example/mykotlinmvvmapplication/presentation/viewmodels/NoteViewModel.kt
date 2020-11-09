package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.domain.entities.Color
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class NoteViewModel(private val interactor: INotesInteractor) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Default + Job() }

    private lateinit var jobGetNote:Job
    private lateinit var jobSaveNote: Job
    private lateinit var jobDeleteNote: Job

    private val successChannel = Channel<Note?>()
    private val errorChannel = Channel<Throwable>()

    private val clickOnHomeLiveData = MutableLiveData<Boolean>()
    private val clickOnColorLiveData = MutableLiveData<Boolean>()
    private val clickOnDeleteLiveData = MutableLiveData<Boolean>()
    private val successDeleteChannel = Channel<Unit?>()

    private var id: String? = null

    private var pendingNote: Note? = null

    fun save(title: String, message: String, color: Color, note: Note?) {

        if (title.length < 3) return

        val newNote = note?.copy(
                title = title,
                text = message,
                lastChanged = Date(),
                color = color
        ) ?: Note(
                UUID.randomUUID().toString(),
                title = title,
                text = message,
                lastChanged = Date(),
                color = color
        )

        pendingNote = newNote
    }

    fun setColor(): Color {
        val list = Color.values()
        list.shuffle()
        return list.first()
    }

    fun updateNote() {
        jobSaveNote = launch {
            pendingNote?.let {
                interactor.saveNote(it)
            }
        }
    }

    fun getSuccessChannel(): ReceiveChannel<Note?> = successChannel

    fun getErrorChannel(): ReceiveChannel<Throwable> = errorChannel

    fun getNoteById(id: String) {
        this.id = id
        jobGetNote = launch {
            try {
                interactor.getNoteById(id)?.let {
                    pendingNote = it
                    successChannel.send(it)
                }
            } catch (e: Throwable) {
                errorChannel.send(e)
            }
        }
    }

    fun clickOnHome() {
        clickOnHomeLiveData.value = true
    }

    fun getClickOnHomeLiveData() = clickOnHomeLiveData

    fun clickOnColor() {
        clickOnColorLiveData.value = true
    }

    fun getClickOnColorLiveData() = clickOnColorLiveData

    fun clickOnDelete() {
        clickOnDeleteLiveData.value = true
    }

    fun getClickOnDeleteLiveData() = clickOnDeleteLiveData

    fun confirmedDelete(id: String?) {
        id?.let {
            jobDeleteNote = launch {
                try {
                    pendingNote?.let { successDeleteChannel.send(interactor.deleteNoteById(id)) }
                    pendingNote = null
                } catch (e: Throwable){
                    errorChannel.send(e)
                }

            }
        }
    }

    fun getSuccessDeleteChannel(): ReceiveChannel<Unit?> = successDeleteChannel

    override fun onCleared() {
        jobGetNote.cancel()
        jobSaveNote.cancel()
        jobDeleteNote.cancel()
        super.onCleared()
    }
}