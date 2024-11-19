package com.explore.parakram24.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.explore.parakram24.EventData
import kotlinx.coroutines.launch

class EventsViewModel(application: Application) : AndroidViewModel(application) {
    private val _eventData = MutableLiveData<List<EventData>>()
    val eventData: LiveData<List<EventData>> get() = _eventData
    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> get() = _loading

    fun fetchData(){
        viewModelScope.launch {
            _loading.value=true
            _eventData.value = listOf(
                EventData("cricket","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461691/cricketer-field-batting-position_53876-137679_l83jhj.jpg"),
                EventData("Badminton","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461642/badminton-concept-with-shuttlecock-racket_23-2149940871_bjuyql.jpg"),
                EventData("Football","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461693/fit-woman-playing-with-soccer-ball_23-2148298917_ub9jr6.jpg"),
                EventData("Hockey","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461692/field-hockey-player-training-practicing-sport-grass_23-2149668566_1_iaqcsj.jpg"),
                EventData("Volleyball","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461699/young-volleyball-man-player-court_23-2149492347_t9j82e.jpg"),
                EventData("Basketball","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461694/full-length-portrait-basketball-player-with-ball_155003-12957_unasl2.jpg"),
                EventData("Kabaddi","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709462173/183794-wueheptwzy-1670041869_yeyrbj.jpg"),
                EventData("Athletics","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461695/muscular-male-young-runner-start-line_23-2148162084_dj73xa.jpg"),
                EventData("Table tennis","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461698/top-view-ping-pong-set-with-copy-space_23-2148523331_ylafzu.jpg"),
                EventData("Squash","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461697/squash-player-hitting-ball_23-2147601830_u7c4ec.jpg"),
                EventData("Chess","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461641/black-white-chess-pieces-black-background_23-2148952321_drexzi.jpg"),
                EventData("Tennis","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461697/racket-tennis-ball-sport-equipment-concept_53876-31460_rocpia.jpg"),
                EventData("Power lifting","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461699/woman-lifting-barbell-gym_23-2147671912_xp2mme.jpg"),
                EventData("Boxing","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709467285/man-with-red-gloves-training_23-2148416707_yebo8t.jpg"),
                EventData("Karate","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709461641/caucasian-martial-arts-fighter-practicing_23-2148446207_g3vltj.jpg"),
            )
            _loading.value=false
        }
    }

}

//                EventData("Boxing","https://res.cloudinary.com/dgpgsuay1/image/upload/v1709462363/Kick-Boxer-Share-The-Ramcharan-Photo-In-Social-Media-About-RRR-Charan-Boxing_m3jo78.jpg"),