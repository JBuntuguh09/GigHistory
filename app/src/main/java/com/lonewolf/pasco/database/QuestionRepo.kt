package com.lonewolf.pasco.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class QuestionRepo( private val questioDao: QuestioDao,  nVal:String, tVal:String) {
    //private late init var questioDao: QuestioDao
      val liveData: LiveData<List<Question>> = questioDao.getAllQues(nVal, tVal)
    val liveDataSectionB: LiveData<List<Question>> = questioDao.getAllQues(nVal, tVal)



    suspend fun insert(question: Question){
        questioDao.insertQues(question)
    }

    suspend fun deleteAll(nVal:String, tVal: String){
        questioDao.deleteAllQues(nVal, tVal)
    }

    suspend fun deleteQuestion(question: Question){
        questioDao.deleteQues(question)
    }

}