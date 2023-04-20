package com.lonewolf.pasco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Question::class, Answers::class, Topics::class, Dictionary::class, Notes::class, Quiz::class], version = 8)

abstract class QuestionDb : RoomDatabase() {

    abstract fun questioDao() :QuestioDao
    abstract fun answerDao() :AnswersDao
    abstract fun topicDao() :TopicDao
    abstract fun dictionaryDao() : DictionaryDao
    abstract fun notesDao() : NotesDao
    abstract fun quizDao() : QuizDao


    companion object {
        @Volatile
        var instance : QuestionDb? = null

        fun getInstance(context: Context): QuestionDb {
            val tempInstance = instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this) {
                    val nInstance = Room.databaseBuilder(
                        context.applicationContext,
                        QuestionDb::class.java,
                        "question_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                instance = nInstance
                return nInstance
            }


        }
    }
}