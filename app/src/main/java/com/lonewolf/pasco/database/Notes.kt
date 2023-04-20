package com.lonewolf.pasco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes (
    @PrimaryKey (autoGenerate = true)
    val Note_Id : Int,
    val Topic : String,
    val Sub_Topic : String,
    val Content : String,
    val Priority : Int,
    val CreatedDatetime : String
        )
