package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestioDao {


    @Insert
    fun insertQues(question: Question)

    @Update
    fun updateQues(question: Question)

    @Delete
    fun deleteQues(question: Question)

    @Query("delete from QUESTION where topic=:vala and type=:tType"  )
    fun deleteAllQues(vala:String, tType: String)


    @Query("select * from QUESTION where topic == :vala and type==:tType ORDER by questionId desc")
    fun getAllQues(vala:String, tType:String): LiveData<List<Question>>


}