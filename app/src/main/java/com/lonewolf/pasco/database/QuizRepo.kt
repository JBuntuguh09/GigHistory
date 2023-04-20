package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData

class QuizRepo(private val quizDao : QuizDao,  nVal:String) {
    val liveDataAll: LiveData<List<Quiz>> = quizDao.getAllQuiz()
    val liveDataSingle: LiveData<List<Quiz>> = quizDao.getQuizType(nVal)



    suspend fun insert(quiz: Quiz){
        quizDao.insert(quiz)
    }

    suspend fun deleteAll(){
        quizDao.deleteAll()
    }

    suspend fun getAllQuiz(nVal : String){
        quizDao.getQuizAllType(nVal)
    }


}