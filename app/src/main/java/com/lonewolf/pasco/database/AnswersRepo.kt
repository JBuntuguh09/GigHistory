package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData

class AnswersRepo(private val answersDao: AnswersDao, nVal : String, tVal : String, aVal : String) {
    val liveData: LiveData<List<Answers>> = answersDao.getAllAns(nVal, tVal, aVal)
    val liveAns: LiveData<List<Answers>> = answersDao.getAns(nVal )

    suspend fun insert(answers: Answers){
        answersDao.insert(answers)
    }




}