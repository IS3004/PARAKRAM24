package com.explore.parakram24.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.explore.parakram24.NotificationData
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

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val _notificationData = MutableLiveData<List<NotificationData>>()
    val notificationData : LiveData<List<NotificationData>> get() = _notificationData

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean> get() = _dataLoaded
    private lateinit var database: DatabaseReference


    fun fetchData() {
        _loading.value = true
        _dataLoaded.value= true
        viewModelScope.launch {
            try {
                Log.i("currentTime in fetch data :", Date().toString())
                database = Firebase.database.reference
                database.child("notifications").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val newData = mutableListOf<NotificationData>()
                            for (k in snapshot.children) {
                                try {
                                    val data = k.getValue(NotificationData::class.java) ?: NotificationData()
                                    Log.i("notification",data.toString())
                                    newData.add(data)
                                } catch (e: Error) {
                                    Log.i("error", e.message.toString())
                                }
                            }
                            viewModelScope.launch {
                                delay(500) // Delay for 500 milliseconds
                                _loading.value = false
                            }
                            newData.reverse()
                            _notificationData.value = newData
                            _dataLoaded.value = true //i want to update this only after 500 millis delay
                        }
                        else{
                            viewModelScope.launch {
                                delay(500) // Delay for 500 milliseconds
                                _loading.value = false
                            }
                            _dataLoaded.value = false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.i("error", error.message)
                        viewModelScope.launch {
                            delay(500) // Delay for 500 milliseconds
                            _loading.value = false
                        }
                        _dataLoaded.value = false
                    }

                })


            } catch (e: IOException) {
                Log.d("IndividualEventsViewModel", e.toString())
                _loading.value = false
            }
        }
    }



}