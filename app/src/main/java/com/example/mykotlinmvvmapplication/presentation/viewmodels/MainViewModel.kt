package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainViewModel(private val interactor: INotesInteractor)
    : ViewModel(),CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Default + Job()
    }

    private val successChannel = Channel<List<Note>?>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    private val clickOnFabLiveData = MutableLiveData<Boolean>()
    private val clickOnLogoutLiveData = MutableLiveData<Boolean>()
    private val clickOnNoteLiveData = MutableLiveData<String>()

    init {
        launch {
            interactor.getNotes().consumeEach {
                when (it) {
                    is NoteResult.Success<*> -> successChannel.send(it.data as? List<Note>?)
                    is NoteResult.Error -> errorChannel.send(it.error)
                }
            }
        }
    }

    fun getSuccessChannel(): ReceiveChannel<List<Note>?> = successChannel

    fun getErrorChannel(): ReceiveChannel<Throwable> = errorChannel

    fun clickOnFab() {
        clickOnFabLiveData.value = true
    }

    fun getClickOnFabLiveData(): LiveData<Boolean> = clickOnFabLiveData

    fun clickOnLogout() {
        clickOnLogoutLiveData.value = true
    }

    fun getClickOnLogoutLiveData(): LiveData<Boolean> = clickOnLogoutLiveData

    fun clickOnNote(id: String) {
        clickOnNoteLiveData.value = id
    }

    fun getClickOnNoteLiveData(): LiveData<String> = clickOnNoteLiveData

    @VisibleForTesting
    public override fun onCleared() {
        cancel()
        super.onCleared()
    }
}