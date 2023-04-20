package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuesViewModel(application: Application) :AndroidViewModel(application) {

     val liveData: LiveData<List<Question>>
    private val repo : QuestionRepo

    init {
        val questioDa = QuestionDb.getInstance(application).questioDao()
        val storage = Storage(application)
        repo = QuestionRepo(questioDa, storage.project!!, storage.selectedCategory!!)
        liveData = repo.liveData

    }

      fun insert(question: Question){
          viewModelScope.launch(Dispatchers.IO) {
              repo.insert(question)
          }
    }

    fun deleteAll(nVal : String, tVal:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll(nVal, tVal)
        }
    }

    fun deleteQuestion(question: Question){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteQuestion(question)
        }
    }

}