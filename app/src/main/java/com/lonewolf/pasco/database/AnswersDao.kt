package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnswersDao {
    @Insert
    fun insert(answers: Answers)

    @Update
    fun update(answers: Answers)

    @Delete
    fun delete(answers: Answers)

    @Query("select * from ANSWERS where topic == :vala and type==:tType and answerTitle==:aTitle ORDER by qNum * 1 asc")
    fun getAllAns(vala:String, tType:String, aTitle:String): LiveData<List<Answers>>

    @Query("select * from ANSWERS where answerId == :vala ORDER by answerId desc")
    fun getAns(vala:String): LiveData<List<Answers>>


}