package com.openweatherapp.restclient

import com.openweatherapp.model.CurrentLocationWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("data/2.5/forecast/daily")
    fun fetchWeather(@Query("q") city: String,@Query("units") metric:String,@Query("appid") appid: String): Single<CurrentLocationWeather>
}