package com.example.mykotlinmvvmapplication.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mykotlinmvvmapplication.data.repositoty.IData
import com.example.mykotlinmvvmapplication.domain.entities.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class APIAnswer : IData {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val notesReference = firestore.collection(NOTES_COLLECTION)

    override fun getNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
            querySnapshot?.let { snapshot ->
                val notes = snapshot.documents.map { it.toObject(Note::class.java) }
                result.value = NoteResult.Success(notes)
            }
            firebaseFirestoreException?.let {
                result.value = NoteResult.Error(it)
            }
        }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
            notesReference.document(note.id).set(note)
                    .addOnCompleteListener{
                        result.value = NoteResult.Success(note)
                    }.addOnFailureListener{
                        result.value = NoteResult.Error(it)
                    }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener { snapshot ->
                    val note = snapshot.toObject(Note::class.java)
                    result.value = NoteResult.Success(note)
                }.addOnFailureListener {
                    result.value = NoteResult.Error(it)
                }
        return result
    }


}