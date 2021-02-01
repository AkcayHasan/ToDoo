package com.hasanakcay.todoo.service

import retrofit2.http.GET

interface WeatherAPI {

    //api.openweathermap.org/data/2.5/weather?q=Istanbul&appid=1217b60ef51690a245a8c2dcbfd0c959
    //@GET işlemi
    @GET("/weather?q=Istanbul&appid=1217b60ef51690a245a8c2dcbfd0c959")
    fun getWeatherData()

}