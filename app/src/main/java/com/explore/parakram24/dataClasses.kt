package com.explore.parakram24

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CoreTeamData(val name : String = "", val position : String= "", val team : String= "", val image : String= "")

@Keep
data class MatchData(
    @SerializedName("key") val key: String = "",
    @SerializedName("date") val date: String = "Date",
    @SerializedName("league") val league : String = "League",
    @SerializedName("likeA") val likeA: String = "0",
    @SerializedName("likeB") val likeB: String = "0",
    @SerializedName("score") val score: ScoreData = ScoreData(),
    @SerializedName("teamAImage") val teamAImage: String = "",
    @SerializedName("teamAname") val teamAname: String = "TEAM A",
    @SerializedName("teamBImage") val teamBImage: String = "",
    @SerializedName("teamBname") val teamBname: String = "TEAM B",
    @SerializedName("time") val time : String = "Time",
    @SerializedName("venue") val venue: String = "Venue"
)
@Keep
data class ScoreData(
    val leftField1 : String = "0",
    val leftField2 :String = "0",
    val leftField3: String = "0",
    val field1 : String = "0",
    val field2 : String = "0",
    val field3: String = "0",
    val rightField1 : String = "0",
    val rightField2 : String = "0",
    val rightField3: String = "0",
)


@Keep
data class EventData(val name: String= "",val image: String="")

@Keep
data class SponsorData(val image: String = "", val link: String = "")

@Keep
data class  NotificationData(val title : String= "", val body : String ="")