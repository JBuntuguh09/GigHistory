package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {

    @Insert
    fun insertNote(notes: Notes)

    @Update
    fun updateNote(notes: Notes)

    @Delete
    fun deleteNote(notes: Notes)

    @Query("select * from notes where Topic= :cCat order by Priority asc")
    fun getAllNotes(cCat : String) : LiveData<List<Notes>>


    @Query("select * from notes where Note_Id = :nId order by Priority asc")
    fun getNote(nId :Int) : LiveData<List<Notes>>

    @Query("delete from notes where Topic = :selectedTopic")
    fun deleteAll(selectedTopic:String)

}