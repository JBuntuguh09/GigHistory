package com.lonewolf.pasco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question")
data class Question (

    @PrimaryKey(autoGenerate = true)
    val questionId :Int,
    val question:String,
    val ans:String,
    val ansA:String,
    val ansB:String,
    val ansC:String,
    val ansD:String,
    val qNum:String,
    val topic:String,
    val type:String
    )

