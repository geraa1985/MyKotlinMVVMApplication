package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockInteractor = mockk<INotesInteractor>()

    private val notesChannel = Channel<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockInteractor.getNotes() } returns notesChannel
        viewModel = MainViewModel(mockInteractor)
    }


    @Test
    fun `should getNotes once`() = runBlocking {
        coVerify(exactly = 1) { mockInteractor.getNotes() }
    }


    @Test
    fun `getSuccessChannel should return Notes`() = runBlocking {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        val job = launch(Dispatchers.IO) {
            viewModel.getSuccessChannel().receive().let {
                result = it
            }
        }
        notesChannel.send(NoteResult.Success(testData))
        while (!job.isCompleted){
            delay(10)
        }
        assertEquals(testData, result)
    }


    @Test
    fun `getErrorChannel should return error`() = runBlocking{
        val testData = Exception()
        val def = async(Dispatchers.IO) {
            viewModel.getErrorChannel().receive()
        }
        notesChannel.send(NoteResult.Error(testData))
        val result = def.await()
        assertEquals(testData, result)
    }

    @Test
    fun `getClickOnFab return true`() {
        var result: Boolean? = null
        viewModel.getClickOnFabLiveData().observeForever {
            result = it
        }
        viewModel.clickOnFab()
        assertEquals(true, result)
    }

//    @Test
//    fun `should remove observer`() {
//        viewModel.onCleared()
//        assertFalse(notesLiveData.hasObservers())
//    }

}