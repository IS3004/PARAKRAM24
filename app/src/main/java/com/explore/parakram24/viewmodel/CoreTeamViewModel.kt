package com.explore.parakram24.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.explore.parakram24.CoreTeamData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class CoreTeamViewModel(application: Application) : AndroidViewModel(application) {
    private val _coreTeamData = MutableLiveData<List<CoreTeamData>>()
    val coreTeamData : LiveData<List<CoreTeamData>> get() = _coreTeamData

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading

    fun fetchData(){
        viewModelScope.launch {
            try {
                _loading.value = true

                val assetManager = getApplication<Application>().assets
                val inputStream: InputStream = assetManager.open("coreTeamJSON.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                val json = String(buffer, Charsets.UTF_8)
                val gson = Gson()
                val coreTeamData = gson.fromJson(json, Array<CoreTeamData>::class.java)
                _coreTeamData.value = coreTeamData.toList()
                _loading.value = false
            } catch (e: IOException) {
                Log.d("CoreTeamViewModel", e.toString())
                _loading.value = false
            }
        }
    }
}