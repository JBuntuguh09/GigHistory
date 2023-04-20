package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuizDao {

    @Insert
    fun insert(quiz: Quiz)

    @Update
    fun update(quiz: Quiz)

    @Query("select * from quiz where type == :nVal")
    fun getQuizType(nVal : String) : LiveData<List<Quiz>>

    @Query("select * from quiz")
    fun getAllQuiz() : LiveData<List<Quiz>>

    @Query("delete from quiz")
    fun deleteAll()

    @Query("select * from quiz where type == :nVal")
    fun getQuizAllType(nVal : String) : LiveData<List<Quiz>>


}