package com.lonewolf.pasco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topics (
    @PrimaryKey(autoGenerate = true)
    val topicId : Int,
    val topic : String,
    val type : String
        )