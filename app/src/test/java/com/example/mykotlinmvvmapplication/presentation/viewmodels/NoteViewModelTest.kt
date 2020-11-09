package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockInteractor = mockk<INotesInteractor>()
    private lateinit var viewModel: NoteViewModel
    private val testNote = Note("1", "title1", "text1")

    @Before
    fun setup() {
        clearAllMocks()
        viewModel = NoteViewModel(mockInteractor)
    }

    @Test
    fun `getNoteById return success`() = runBlocking {
        viewModel.getNoteById(testNote.id)
        delay(1000)
        coVerify(exactly = 1) { mockInteractor.getNoteById(testNote.id) }
    }

    @Test
    fun `confirmDeleteById return success`() = runBlocking{
        coEvery { mockInteractor.getNoteById(testNote.id) } returns testNote
        val jobGet = launch(Dispatchers.IO) { viewModel.getNoteById(testNote.id) }
        while (!jobGet.isCompleted){
            delay(10)
        }
        val jobDelete = launch(Dispatchers.IO) {viewModel.confirmedDelete(testNote.id)}
        while (!jobDelete.isCompleted){
            delay(10)
        }
        coVerify(exactly = 1) { mockInteractor.deleteNoteById(testNote.id) }
    }


    @Test
    fun `updateNote should save changes`() = runBlocking {
        coEvery { mockInteractor.getNoteById(testNote.id) } returns testNote
        coEvery { mockInteractor.saveNote(any()) } returns mockk()

        val jobGet = launch(Dispatchers.IO) { viewModel.getNoteById(testNote.id) }
        while (!jobGet.isCompleted){
            delay(10)
        }

        val jobSave = launch(Dispatchers.IO) { viewModel.updateNote() }
        while (!jobSave.isCompleted){
            delay(10)
        }

        coVerify(exactly = 1) { mockInteractor.saveNote(testNote) }
    }

}