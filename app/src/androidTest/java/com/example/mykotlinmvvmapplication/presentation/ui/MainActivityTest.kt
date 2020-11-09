package com.example.mykotlinmvvmapplication.presentation.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.presentation.adapters.NotesRVAdapter
import com.example.mykotlinmvvmapplication.presentation.viewmodels.MainViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext

@ExperimentalCoroutinesApi
class MainActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val viewModel: MainViewModel = mockk(relaxed = true)
    private val successChannel = Channel<List<Note>?>(Channel.CONFLATED)

    private val testNotes = listOf(
            Note("1", "title1", "text1"),
            Note("2", "title2", "text2"),
            Note("3", "title3", "text3"),
    )

    @Before
    fun setup() {
        StandAloneContext.loadKoinModules(
                listOf(
                        module {
                            viewModel { viewModel }
                        }
                )
        )
        every { viewModel.getSuccessChannel() } returns successChannel
        activityTestRule.launchActivity(null)
        successChannel.offer(testNotes)
    }

    @After
    fun tearDown() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        Espresso.onView(withId(R.id.rv_notes)).perform(RecyclerViewActions.scrollToPosition<NotesRVAdapter.ViewHolder>(1))
        Espresso.onView(withText(testNotes[1].text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}