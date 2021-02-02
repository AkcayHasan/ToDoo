package com.hasanakcay.todoo.service

import com.hasanakcay.todoo.model.Main
import com.hasanakcay.todoo.model.Weather
import io.reactivex.Single
import retrofit2.http.GET

interface WeatherAPI {

    //api.openweathermap.org/data/2.5/weather?q=Istanbul&appid=1217b60ef51690a245a8c2dcbfd0c959

    @GET("/weather?q=Istanbul&appid=1217b60ef51690a245a8c2dcbfd0c959")
    suspend fun getWeatherData() : Single<Weather>

    @GET("/weather?q=Istanbul&appid=1217b60ef51690a245a8c2dcbfd0c959")
    suspend fun getTempData() : Single<Main>

}