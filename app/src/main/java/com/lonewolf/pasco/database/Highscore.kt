package com.lonewolf.pasco.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Highscore")
 data class Highscore(
    @PrimaryKey(autoGenerate = true)
    val highscoreId :Int,
    val answerId:String,
    val date:String,
    val score:String,
    val subject:String,
    val type:String,
    val userId : String
)