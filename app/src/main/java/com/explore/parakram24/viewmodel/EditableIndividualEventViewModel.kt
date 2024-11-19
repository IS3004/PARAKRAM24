package com.explore.parakram24.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.explore.parakram24.EventData
import com.explore.parakram24.MatchData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date

class EditableIndividualEventViewModel(application: Application) : AndroidViewModel(application) {
    private val _etGames = MutableLiveData<MutableMap<String,List<MatchData>>>()
    private val _loading = MutableLiveData<Boolean>()
    val etGames : LiveData<MutableMap<String, List<MatchData>>> get() = _etGames
    val loading : LiveData<Boolean> get() = _loading
    private lateinit var database: DatabaseReference

    fun fetchData(fragment: String){
        viewModelScope.launch {
            try {
                _loading.value = true
                Log.i("currentFragment", fragment)
                database = Firebase.database.reference
                database.child(fragment).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val newData = mutableListOf<MatchData>()
                            for (k in snapshot.children) {
                                try {
                                    val data = k.getValue(MatchData::class.java) ?: MatchData()
                                    newData.add(data)
                                    Log.i("data",k.value.toString())
                                }catch (e :Error  ){
                                    Log.i("error",e.message.toString())
                                }

                            }
                            newData.reverse()
                            addGameData(fragment,newData)
                        }
                        _loading.value = false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.i("error",error.message)
                        _loading.value = false
                    }
                })


            } catch (e: IOException) {
                Log.d("IndividualEventsViewModel", e.toString())
                _loading.value = false
            }
        }
    }

    fun addNewGame(fragment: String){
        val currentDate = Date()
        val newMatchData = MatchData(key = currentDate.toString().substring(4))
        database.child(fragment).child(currentDate.toString().substring(4)).setValue(newMatchData)
    }

    fun addGameData(gameKey: String, list : MutableList<MatchData>) {
        val currentMap = _etGames.value ?: mutableMapOf()
        currentMap[gameKey] = list
        _etGames.value = currentMap
    }

    fun update(fragment : String, cardKey : String, fieldUpdated : String, updatedValue : String){
        database.child(fragment).child(cardKey).child(fieldUpdated).setValue(updatedValue)
    }

    fun update(fragment: String,matchData: MatchData){
        database.child(fragment).child(matchData.key).setValue(matchData)
    }

    fun delete(fragment: String, key : String){
        database.child(fragment).child(key).setValue(null)
    }

    fun getColleges() : MutableMap<String, String>{
        val newData = mutableMapOf<String,String>()
        Firebase.database.reference.child("colleges").get().addOnSuccessListener {
            //Log.i("data",it.value.toString())

            for (k in it.children) {
                try {
                    newData[k.key ?: "IIT ISM"] = k.value.toString()
                    Log.i("data",k.value.toString())
                }catch (e :Error  ){
                    Log.i("error",e.message.toString())
                }

            }
        }
        return newData
    }
}