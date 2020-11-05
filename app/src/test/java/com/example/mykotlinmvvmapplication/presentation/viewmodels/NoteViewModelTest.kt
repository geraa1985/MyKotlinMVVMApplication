package com.example.mykotlinmvvmapplication.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mykotlinmvvmapplication.data.network.NoteResult
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.usecases.INotesInteractor
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockInteractor = mockk<INotesInteractor>()
    private lateinit var viewModel: NoteViewModel
    private val testNote = Note("1", "title1", "text1")
    private val noteLiveData = MutableLiveData<NoteResult>()

    @Before
    fun setup() {
        clearAllMocks()

        every { mockInteractor.saveNote(any()) } returns noteLiveData
        every { mockInteractor.getNoteById(testNote.id) } returns noteLiveData
        every { mockInteractor.deleteNoteById(testNote.id) } returns noteLiveData

        viewModel = NoteViewModel(mockInteractor)
    }

    @Test
    fun `getNoteById return success`() {
        var result: Note? = null
        val testData = testNote

        viewModel.getSuccessLiveData().observeForever {
            result = it
        }
        viewModel.getNoteById(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)

        assertEquals(testData, result)
    }

    @Test
    fun `getNoteById return error`() {
        var result: Throwable? = null
        val testData = Exception()

        viewModel.getErrorLiveData().observeForever {
            result = it
        }
        viewModel.getNoteById(testNote.id)
        noteLiveData.value = NoteResult.Error(testData)

        assertEquals(testData, result)
    }

    @Test
    fun `deleteNoteById return success`() {
        var result: Boolean? = null

        viewModel.getSuccessDeleteLiveData().observeForever {
            result = it
        }

        viewModel.confirmedDelete(testNote.id)
        noteLiveData.value = NoteResult.Success(null)

        assertEquals(true, result)
    }


    @Test
    fun `deleteNoteById return error`() {
        var result: Throwable? = null
        val testData = Exception()

        viewModel.getErrorLiveData().observeForever {
            result = it
        }

        viewModel.confirmedDelete(testNote.id)
        noteLiveData.value = NoteResult.Error(testData)

        assertEquals(testData, result)
    }

    @Test
    fun `updateNote should save changes`() {
        viewModel.getNoteById(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)
        viewModel.updateNote()
        verify(exactly = 1) { mockInteractor.saveNote(testNote) }
    }

}