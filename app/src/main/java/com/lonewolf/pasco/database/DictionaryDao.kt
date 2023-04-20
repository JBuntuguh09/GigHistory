package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DictionaryDao {

    @Insert
    fun insert(dictionary: Dictionary)

    @Update
    fun update(dictionary: Dictionary)

    @Delete
    fun delete(dictionary: Dictionary)

    @Query("select * from dictionary   ORDER by Word asc ")
    fun getAllDic(): LiveData<List<Dictionary>>

    @Query("select * from dictionary where word like '%' || :vala ||'%'")
    fun getAns(vala:String): LiveData<List<Dictionary>>

    @Query("select * from dictionary where word like '%' || :vala ||'%'")
    fun getDic(vala:String): List<Dictionary>

    @Query("Delete from dictionary")
    fun deleteAll()


}