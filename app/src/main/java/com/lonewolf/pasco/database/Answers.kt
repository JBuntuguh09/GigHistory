package com.lonewolf.pasco.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
 data class Answers (
    @PrimaryKey(autoGenerate = true)
    val answerId :Int,
    val question:String,
    val ans:String,
    val ansSelected:String,
    val ansA:String,
    val ansB:String,
    val ansC:String,
    val ansD:String,
    val qNum:String,
    val topic:String,
    val type:String,
    val answerTitle:String ,
    val createdDatetime:String

)