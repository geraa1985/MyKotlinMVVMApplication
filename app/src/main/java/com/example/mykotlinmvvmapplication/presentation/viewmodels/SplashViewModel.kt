package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SplashViewModel(private val interactor: INotesInteractor) : ViewModel(), CoroutineScope {

    private val successChannel = Channel<User?>()
    private val errorChannel = Channel<Throwable>()

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Default + Job() }
    private lateinit var jobGetUser: Job

    fun getSuccessChannel(): ReceiveChannel<User?> = successChannel

    fun getErrorChannel(): ReceiveChannel<Throwable> = errorChannel

    fun requestUser() {
        jobGetUser = launch {
            try {
                successChannel.send(interactor.getUser())
            } catch (e: Throwable) {
                errorChannel.send(e)
            }
        }
    }

    override fun onCleared() {
        jobGetUser.cancel()
        super.onCleared()
    }
}