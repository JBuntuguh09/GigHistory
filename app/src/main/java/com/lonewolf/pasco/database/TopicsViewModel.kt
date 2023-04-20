package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicsViewModel(application: Application):AndroidViewModel(application) {
    val liveData: LiveData<List<Topics>>
     var repo : TopicRepo
    val storage = Storage(application)

    init {
        val questioDa = QuestionDb.getInstance(application).topicDao()

        repo = TopicRepo(questioDa, storage.selectedCategory!!)
        liveData = repo.liveData

    }

    fun insert(topics: Topics){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(topics)
        }

    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll(storage.selectedCategory!!)
        }

    }

    fun deleteTopic(topics: Topics){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTopic(topics)
        }

    }

}