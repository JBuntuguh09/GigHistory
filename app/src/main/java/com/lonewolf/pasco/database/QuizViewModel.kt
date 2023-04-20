package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.*
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    val liveData: LiveData<List<Quiz>>
    val liveDataSingle: LiveData<List<Quiz>>
    var repo : QuizRepo
    val storage = Storage(application)

    init {
        val quizDa = QuestionDb.getInstance(application).quizDao()

        repo = QuizRepo(quizDa, storage.randVal!!)
        liveData = repo.liveDataAll
        liveDataSingle = repo.liveDataSingle

    }

    fun insert(quiz: Quiz){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(quiz)
        }

    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }

    }


}