package com.hasanakcay.todoo.service

import com.hasanakcay.todoo.model.Main
import com.hasanakcay.todoo.model.Weather
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "api.openweathermap.org/data/2.5"

class WeatherAPIService {
    //api instance oluştur. Bir kere oluşturulacağı için lazy ifadesi ile oluşturuldu.

    private val api : WeatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    suspend fun getWeather(): Single<Weather>{
        return api.getWeatherData()
    }
    suspend fun getTemprature(): Single<Main>{
        return api.getTempData()
    }
}