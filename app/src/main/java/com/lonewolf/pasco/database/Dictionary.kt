package com.lonewolf.pasco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Dictionary (
    @PrimaryKey(autoGenerate = true)
    val DictionaryId:Int,
    val Word :String,
    val Meaning : String,
    val Pic : String,
    val CreatedDateTime : String
        )