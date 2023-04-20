package com.lonewolf.pasco.database

import androidx.lifecycle.LiveData

class DictionaryRepo(private val dictionaryDao: DictionaryDao, nWord :String)  {
    val liveData: LiveData<List<Dictionary>> = dictionaryDao.getAllDic()
    val liveDataSearch: LiveData<List<Dictionary>> = dictionaryDao.getAns(nWord)

    suspend fun insert(dictionary: Dictionary){
        dictionaryDao.insert(dictionary)
    }

    suspend fun deleteAll(){
        dictionaryDao.deleteAll()
    }
}