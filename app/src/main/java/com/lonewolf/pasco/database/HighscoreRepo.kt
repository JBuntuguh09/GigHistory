package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData

class HighscoreRepo(private val highscoreDao: HighscoreDao, userId : String) {

    val liveData: LiveData<List<Highscore>> = highscoreDao.getUserHighscore(userId)



    suspend fun insert(highscore: Highscore) {
        highscoreDao.insertHigh(highscore)
    }

}