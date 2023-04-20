package com.lonewolf.pasco.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val liveData : LiveData<List<Notes>>
    val liveSingle : LiveData<List<Notes>>
    val storage = Storage(application)

    private var repo : NotesRepo
    init {
        val sNotes = QuestionDb.getInstance(application).notesDao()

        var rand = 0
        try {
            if(storage.randVal!! != ""){
                rand = storage.randVal!!.toInt()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        repo = NotesRepo(sNotes, rand, storage.fragVal!!)
        liveData = repo.liveData
        liveSingle = repo.liveDataNote

    }

    fun insert(notes: Notes){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(notes)
        }
    }

    fun deleteAll(selectedTopic: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll(selectedTopic)
        }
    }

}