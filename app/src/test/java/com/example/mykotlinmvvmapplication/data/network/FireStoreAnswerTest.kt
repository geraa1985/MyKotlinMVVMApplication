package com.example.mykotlinmvvmapplication.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FireStoreAnswerTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockRefCollection = mockk<CollectionReference>()

    private val mockDoc1 = mockk<DocumentSnapshot>()
    private val mockDoc2 = mockk<DocumentSnapshot>()
    private val mockDoc3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    private val provider = FireStoreAnswer(mockAuth, mockDb)

    @Before
    fun setup() {
        clearAllMocks()

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockRefCollection

        every { mockDoc1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDoc2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDoc3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `getNotes should throw NoAuthException if no auth`() = runBlocking {
        every { mockAuth.currentUser } returns null
        val result = provider.getNotes().receive()
        assertTrue(result is NoteResult.Error && result.error is NoAuthExceptions)
    }

    @Test
    fun `getNotes should return notes`() = runBlocking {

        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockDoc1, mockDoc2, mockDoc3)
        every {
            mockRefCollection.orderBy("lastChanged", Query.Direction.DESCENDING)
                    .addSnapshotListener(capture(slot))
        } returns mockk()

        val def = async(Dispatchers.IO) {
            provider.getNotes().receive().let {
                (it as NoteResult.Success<List<Note>>).data
            }
        }

        delay(1000)
        slot.captured.onEvent(mockSnapshot, null)

        val result = def.await()
        assertEquals(testNotes, result)
    }

    @Test
    fun `getNotes should return error`() = runBlocking {
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockRefCollection.orderBy("lastChanged", Query.Direction.DESCENDING).addSnapshotListener(capture(slot)) } returns mockk()

        val def = async(Dispatchers.IO) {
            provider.getNotes().receive().let {
                (it as NoteResult.Error).error
            }
        }
        delay(1000)
        slot.captured.onEvent(null, testError)

        val result = def.await()
        assertEquals(testError, result)

    }

    @Test
    fun `saveNote calls set`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        val slot = slot<OnSuccessListener<Void>>()
        every {
            mockDocumentReference.set(testNotes[0])
                    .addOnFailureListener(any())
                    .addOnSuccessListener(capture(slot))
        } returns mockk()
        launch(Dispatchers.IO) {
            provider.saveNote(testNotes[0])
        }
        delay(1000)
        slot.captured.onSuccess(null)
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote returns note`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testNotes[0])
                    .addOnFailureListener(any())
                    .addOnSuccessListener(capture(slot))
        } returns mockk()

        val def = async(Dispatchers.IO) {
            provider.saveNote(testNotes[0])
        }

        delay(1000)
        slot.captured.onSuccess(null)
        val result = def.await()
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `saveNote returns error`() = runBlocking {
        var result: Throwable? = null
        val testError = mockk<Exception>()
        val slot = slot<OnFailureListener>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .set(testNotes[0])
                    .addOnFailureListener(capture(slot))
                    .addOnSuccessListener(any())
        } returns mockk()

        launch(Dispatchers.IO) {
            try {
                provider.saveNote(testNotes[0])
            } catch (e: Throwable) {
                result = e
            }
        }

        delay(1000)
        slot.captured.onFailure(testError)

        delay(3000)
        assertEquals(testError, result)
    }

    @Test
    fun `deleteNoteById calls delete`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        val slot = slot<OnSuccessListener<Void>>()
        every {
            mockDocumentReference.delete()
                    .addOnSuccessListener(capture(slot))
                    .addOnFailureListener(any())
        } returns mockk()
        launch(Dispatchers.IO) {
            provider.deleteNoteById(testNotes[0].id)
        }
        delay(1000)
        slot.captured.onSuccess(null)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

    @Test
    fun `deleteNoteById returns succes`() = runBlocking {
        var result = false
        val slot = slot<OnSuccessListener<Void>>()

        coEvery {
            mockRefCollection
                    .document(testNotes[0].id)
                    .delete()
                    .addOnSuccessListener(capture(slot))
                    .addOnFailureListener(any())
        } returns mockk()

        val job = launch(Dispatchers.IO) {
            provider.deleteNoteById(testNotes[0].id).let {
                result = it == Unit
            }
        }

        while (!slot.isCaptured){
            delay(10)
        }

        slot.captured.onSuccess(null)

        while (!job.isCompleted){
            delay(10)
        }

        assertEquals(true, result)
    }

    @Test
    fun `deleteNoteById returns error`() = runBlocking {
        var result: Throwable? = null
        val testError = mockk<Exception>()
        val slot = slot<OnFailureListener>()

        coEvery {
            mockRefCollection
                    .document(testNotes[0].id)
                    .delete()
                    .addOnSuccessListener(any())
                    .addOnFailureListener(capture(slot))
        } returns mockk()

        val job = launch(Dispatchers.IO) {
            try {
                provider.deleteNoteById(testNotes[0].id)
            } catch (e: Throwable) {
                result = e
            }
        }

        while (!slot.isCaptured){
            delay(10)
        }

        slot.captured.onFailure(testError)

        while (!job.isCompleted){
            delay(10)
        }

        assertEquals(testError, result)
    }

    @Test
    fun `getNoteById calls get`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()
        every {
            mockDocumentReference.get()
                    .addOnSuccessListener(capture(slot))
                    .addOnFailureListener(any())
        } returns mockk()
        launch(Dispatchers.IO) {
            provider.getNoteById(testNotes[0].id)
        }
        while (!slot.isCaptured){
            delay(10)
        }
        slot.captured.onSuccess(mockDoc1)
        verify(exactly = 1) { mockDocumentReference.get() }
    }

    @Test
    fun `getNoteById returns succes`() = runBlocking {
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .get()
                    .addOnSuccessListener(capture(slot))
                    .addOnFailureListener(any())
        } returns mockk()

        val def = async(Dispatchers.IO) {
            provider.getNoteById(testNotes[0].id)
        }
        while (!slot.isCaptured){
            delay(10)
        }
        slot.captured.onSuccess(mockDoc1)
        val result = def.await()
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `getNoteById returns error`() = runBlocking {
        var result: Throwable? = null
        val slot = slot<OnFailureListener>()
        val testError = mockk<Exception>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .get()
                    .addOnSuccessListener(any())
                    .addOnFailureListener(capture(slot))
        } returns mockk()
        val def = async(Dispatchers.IO) {
            try {
                provider.getNoteById(testNotes[0].id)
            } catch (e: Throwable) {
                e
            }
        }
        while (!slot.isCaptured){
            delay(10)
        }
        slot.captured.onFailure(testError)
        result = def.await() as Throwable
        assertEquals(testError, result)
    }

    @Test
    fun `getUser should return user`() = runBlocking {

        every { mockUser.displayName } returns ""
        every { mockUser.email } returns ""

        val result = provider.getCurrentUser()

        assertTrue(result is User)
    }

}