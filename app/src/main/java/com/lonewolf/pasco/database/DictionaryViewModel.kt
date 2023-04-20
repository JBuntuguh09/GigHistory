package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    val liveData : LiveData<List<Dictionary>>
    //
    private var repo : DictionaryRepo
    init {
        val sDic = QuestionDb.getInstance(application).dictionaryDao()

        val storage = Storage(application)


        repo = DictionaryRepo(sDic,  storage.randVal!!)
        liveData = repo.liveData

    }
    fun getAll() : List<Dictionary>{
        var staticData : List<Dictionary> = ArrayList()
        viewModelScope.launch ( Dispatchers.IO ){

        }
        return  staticData
    }

    fun insert(dictionary: Dictionary){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(dictionary)
        }
    }

    fun deleteAll(){
        viewModelScope.launch ( Dispatchers.IO ){
            repo.deleteAll()
        }
    }
}