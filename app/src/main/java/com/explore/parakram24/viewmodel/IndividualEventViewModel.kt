package com.explore.parakram24.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.explore.parakram24.MatchData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date

class IndividualEventViewModel(application: Application) : AndroidViewModel(application) {
    private val _games = MutableLiveData<MutableMap<String, List<MatchData>>>()
    val games: LiveData<MutableMap<String, List<MatchData>>> get() = _games

    private val _eventLoading = MutableLiveData<Boolean>()
    val eventLoading: LiveData<Boolean> get() = _eventLoading
    private lateinit var database: DatabaseReference

    private val _data = MutableLiveData<Boolean>()
    val data: LiveData<Boolean> get() = _data



    fun fetchData(current: String) {
        _eventLoading.value = true
        _data.value = true
        viewModelScope.launch {
            try {
                Log.i("currentTime in fetch data :", Date().toString())
                Log.i("current", current)
                database = Firebase.database.reference
                database.child(current).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val newData = mutableListOf<MatchData>()
                            for (k in snapshot.children) {
                                try {
                                    val data = k.getValue(MatchData::class.java) ?: MatchData()
                                    newData.add(data)
                                } catch (e: Error) {
                                    Log.i("error", e.message.toString())
                                }
                            }
                            viewModelScope.launch {
                                delay(500) // Delay for 500 milliseconds
                                _eventLoading.value = false
                            }
                            newData.reverse()
                            addGameData(current, newData)
                            _data.value = true //i want to update this only after 500 millis delay
                        }
                        else{
                            viewModelScope.launch {
                                delay(500) // Delay for 500 milliseconds
                                _eventLoading.value = false
                            }
                            _data.value = false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.i("error", error.message)
                        viewModelScope.launch {
                            delay(500) // Delay for 500 milliseconds
                            _eventLoading.value = false
                        }
                        _data.value = false
                    }

                })


            } catch (e: IOException) {
                Log.d("IndividualEventsViewModel", e.toString())
                _eventLoading.value = false
            }
        }
    }

    fun addGameData(gameKey: String, list: MutableList<MatchData>?) {
        val currentMap = _games.value ?: mutableMapOf()
        currentMap[gameKey] = list ?: listOf()
        _games.value = currentMap
    }
}
