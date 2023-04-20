package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class answersViewModel(application: Application): AndroidViewModel(application) {
    val liveData : LiveData<List<Answers>>
    private var repo : AnswersRepo
    init {
        val ans = QuestionDb.getInstance(application).answerDao()
        val storage = Storage(application)

        repo = AnswersRepo(ans, storage.project!!, storage.selectedCategory!!, storage.ansTitle!!)
        liveData = repo.liveData
    }

    fun insert(answers: Answers){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(answers)
        }
    }
}