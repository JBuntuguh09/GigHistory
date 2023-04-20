package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lonewolf.pasco.resources.Storage

@Dao
interface TopicDao {
    //Topics
    @Insert
    fun insertTopic(topics: Topics)

    @Delete
    fun deleteTopic(topics: Topics)

    @Query("Delete from topics where type = :cat")
    fun deleteAllObjectTopics(cat:String)

    @Query("select * from topics where type=:idsz")
    fun getAllTopics(idsz: String) : LiveData<List<Topics>>
}