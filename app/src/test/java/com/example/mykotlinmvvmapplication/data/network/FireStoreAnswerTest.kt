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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun `getNotes should throw NoAuthException if no auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.getNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthExceptions)
    }

    @Test
    fun `getNotes should return notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockDoc1, mockDoc2, mockDoc3)
        every { mockRefCollection.orderBy("lastChanged", Query.Direction.DESCENDING).addSnapshotListener(capture(slot)) } returns mockk()

        provider.getNotes().observeForever {
            result = (it as NoteResult.Success<List<Note>>).data
        }

        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, result)

    }

    @Test
    fun `getNotes should return error`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockRefCollection.orderBy("lastChanged", Query.Direction.DESCENDING).addSnapshotListener(capture(slot)) } returns mockk()

        provider.getNotes().observeForever {
            result = (it as NoteResult.Error).error
        }

        slot.captured.onEvent(null, testError)

        assertEquals(testError, result)

    }


    @Test
    fun `saveNote calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote returns note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)).addOnFailureListener(any()) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as NoteResult.Success<Note>).data
        }
        slot.captured.onSuccess(null)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `saveNote returns error`() {
        var result: Throwable? = null
        val testError = mockk<Exception>()
        val slot = slot<OnFailureListener>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .set(testNotes[0])
                    .addOnSuccessListener(any())
                    .addOnFailureListener(capture(slot))
        } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as NoteResult.Error).error
        }
        slot.captured.onFailure(testError)

        assertEquals(testError, result)
    }

    @Test
    fun `deleteNoteById calls delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.deleteNoteById(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

    @Test
    fun `deleteNoteById returns succes`() {
        var result = false
        val slot = slot<OnSuccessListener<Void>>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .delete()
                    .addOnSuccessListener(capture(slot))
                    .addOnFailureListener(any())
        } returns mockk()

        provider.deleteNoteById(testNotes[0].id).observeForever {
            result = (it as NoteResult.Success<*>).data == null
        }

        slot.captured.onSuccess(null)

        assertEquals(true, result)
    }

    @Test
    fun `deleteNoteById returns error`() {
        var result: Throwable? = null
        val testError = mockk<Exception>()
        val slot = slot<OnFailureListener>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .delete()
                    .addOnSuccessListener(any())
                    .addOnFailureListener(capture(slot))
        } returns mockk()

        provider.deleteNoteById(testNotes[0].id).observeForever {
            result = (it as NoteResult.Error).error
        }

        slot.captured.onFailure(testError)

        assertEquals(testError, result)
    }


    @Test
    fun `getNoteById calls get`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockRefCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.getNoteById(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.get() }
    }


    @Test
    fun `getNoteById returns succes`() {
        var result: Note? = null
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()

        every {
            mockRefCollection
                    .document(testNotes[0].id)
                    .get()
                    .addOnSuccessListener(capture(slot))
                    .addOnFailureListener(any())
        } returns mockk()

        provider.getNoteById(testNotes[0].id).observeForever {
            result = (it as NoteResult.Success<Note>).data
        }

        slot.captured.onSuccess(mockDoc1)

        assertEquals(testNotes[0], result)
    }


    @Test
    fun `getNoteById returns error`() {
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

        provider.getNoteById(testNotes[0].id).observeForever {
            result = (it as NoteResult.Error).error
        }

        slot.captured.onFailure(testError)

        assertEquals(testError, result)
    }

    @Test
    fun `getUser should return user`(){
        var result: User? = null

        every { mockUser.displayName } returns ""
        every { mockUser.email } returns ""

        provider.getCurrentUser().observeForever{
            result = it
        }

        assertTrue(result is User)

    }

}