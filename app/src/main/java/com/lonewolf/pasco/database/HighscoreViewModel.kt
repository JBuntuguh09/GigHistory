package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HighscoreViewModel(application: Application) : AndroidViewModel(application) {

    val liveData : LiveData<List<Highscore>>

    val storage = Storage(application)

    private var repo : HighscoreRepo
    init {
        val sHighscore = QuestionDb.getInstance(application).highscoreDao()


        repo = HighscoreRepo(sHighscore, storage.uSERID!!)
        liveData = repo.liveData


    }

    fun insert(highscore: Highscore){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(highscore)
        }
    }


}