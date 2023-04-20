package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData

class TopicRepo(private val topicDao: TopicDao, type:String) {
    val liveData: LiveData<List<Topics>> = topicDao.getAllTopics(type)

    suspend fun insert(topics: Topics){
        topicDao.insertTopic(topics)
    }

    suspend fun deleteAll(types: String){
        topicDao.deleteAllObjectTopics(types)
    }

    suspend fun deleteTopic(topics: Topics){
        topicDao.deleteTopic(topics)
    }
}