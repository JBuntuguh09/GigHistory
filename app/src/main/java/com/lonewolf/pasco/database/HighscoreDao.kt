package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HighscoreDao {

    @Insert
    fun insertHigh(highscore: Highscore)

    @Update
    fun updateHigh(highscore: Highscore)

    @Delete
    fun deleteHigh(highscore: Highscore)

    @Query("select * from highscore where userId = :userId order by date desc")
    fun getUserHighscore(userId : String) : LiveData<List<Highscore>>
}