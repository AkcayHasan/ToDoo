package com.hasanakcay.todoo.model

import com.google.gson.annotations.SerializedName

data class WeatherModel(

    @SerializedName("id")
    val id : Int ?= null,
    @SerializedName("main")
    val main : String ?= null,
    @SerializedName("description")
    val description : String ?= null,
    @SerializedName("icon")
    val icon : String ?= null,

) {
}