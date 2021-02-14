package com.hasanakcay.todoo.model

import com.google.gson.annotations.SerializedName

data class OpenWeatherModel (
    @SerializedName("weather")
    val weather : ArrayList<WeatherModel>,
    @SerializedName("main")
    val main : MainModel
)

