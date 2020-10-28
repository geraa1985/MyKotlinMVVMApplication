package com.example.mykotlinmvvmapplication.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mykotlinmvvmapplication.data.repositoty.IData
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.example.mykotlinmvvmapplication.domain.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FireStoreAnswer : IData {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val user
        get() = FirebaseAuth.getInstance().currentUser

    private val firestore = FirebaseFirestore.getInstance()

    private val userNotesCollection
        get() =
            user?.let {
                firestore.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
            } ?: throw NoAuthExceptions()


    override fun getNotes(): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    userNotesCollection.orderBy("lastChanged", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
                        value = error?.let {
                            throw it
                        } ?: snapshot?.let { querySnapshot ->
                            val notes = querySnapshot.documents.map { it.toObject(Note::class.java) }
                            NoteResult.Success(notes)
                        }
                    }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }

    override fun saveNote(note: Note): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    userNotesCollection.document(note.id).set(note)
                            .addOnSuccessListener {
                                value = NoteResult.Success(note)
                            }.addOnFailureListener {
                                throw it
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }

    override fun getNoteById(id: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    userNotesCollection.document(id).get()
                            .addOnSuccessListener {
                                val note = it.toObject(Note::class.java) as Note
                                value = NoteResult.Success(note)
                            }.addOnFailureListener {
                                throw it
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }

    override fun deleteNoteById(id: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    userNotesCollection.document(id).delete()
                            .addOnSuccessListener {
                                value = NoteResult.Success(null)
                            }.addOnFailureListener {
                                throw it
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }

    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = user?.let {
                    User(it.displayName ?: "", it.email ?: "")
                }
            }
}