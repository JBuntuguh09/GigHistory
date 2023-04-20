package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData

class NotesRepo(private val notesDao: NotesDao, noteId : Int, noteCat : String) {

    val liveData: LiveData<List<Notes>> = notesDao.getAllNotes(noteCat)
    val liveDataNote : LiveData<List<Notes>> = notesDao.getNote(noteId)


    suspend fun insert(notes: Notes){
        notesDao.insertNote(notes)
    }

    suspend fun deleteAll(selectedTopic: String){
        notesDao.deleteAll(selectedTopic)
    }


}