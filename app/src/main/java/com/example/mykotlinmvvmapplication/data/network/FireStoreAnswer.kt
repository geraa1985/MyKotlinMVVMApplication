package com.example.mykotlinmvvmapplication.data.network

import com.example.mykotlinmvvmapplication.data.repositoty.IData
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStoreAnswer(private val fauth: FirebaseAuth, private val fstore: FirebaseFirestore) : IData {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val user
        get() = fauth.currentUser


    private val userNotesCollection
        get() =
            user?.let {
                fstore.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
            } ?: throw NoAuthExceptions()


    override fun getNotes(): ReceiveChannel<NoteResult> =
            Channel<NoteResult>(Channel.CONFLATED).apply {
        try {
            userNotesCollection.orderBy("lastChanged", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
                error?.let {
                    NoteResult.Error(it)
                } ?: snapshot?.let { querySnapshot ->
                    val notes = querySnapshot.documents.map { it.toObject(Note::class.java) }
                    NoteResult.Success(notes)
                }?.let { offer (it) }
            }
        } catch (e: Throwable) {
            offer(NoteResult.Error(e))
        }
    }

    override suspend fun saveNote(note: Note): Note? = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(note.id).set(note)
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
                    .addOnSuccessListener {
                        continuation.resume(note)
                    }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun getNoteById(id: String): Note? = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(id).get()
                    .addOnSuccessListener {
                        val note = it.toObject(Note::class.java) as Note
                        continuation.resume(note)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNoteById(id: String): Unit? = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(user?.let {
            User(it.displayName ?: "", it.email ?: "")
        })
    }
}