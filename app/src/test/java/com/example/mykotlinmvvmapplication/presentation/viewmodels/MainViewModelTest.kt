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
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockInteractor = mockk<INotesInteractor>()

    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockInteractor.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockInteractor)
    }


    @Test
    fun `should getNotes once`() {
        verify(exactly = 1) { mockInteractor.getNotes() }
    }


    @Test
    fun `should return Notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.getSuccessLiveData().observeForever {
            result = it
        }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }


    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Exception()
        viewModel.getErrorLiveData().observeForever {
            result = it
        }
        notesLiveData.value = NoteResult.Error(testData)
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

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }

}