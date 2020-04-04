package com.openweatherapp.restclient

import android.content.Context
import android.os.Build
import androidx.multidex.BuildConfig
import com.openweatherapp.BuildConfig.APP_ID
import com.openweatherapp.model.CurrentLocationWeather
import com.openweatherapp.rxbase.BaseServiceRxWrapper
import io.reactivex.Single

class RetrofitRxWrapper(mContext: Context, apiEndPoint: String) : BaseServiceRxWrapper(mContext,apiEndPoint) {

    private val service: RetrofitService = this.retrofit.create(RetrofitService::class.java)

    fun fetchCurrentLocationWeather(userLocality: String): Single<CurrentLocationWeather> {
        return this.service.fetchWeather(userLocality,"metric",BuildConfig.APP_ID).cache()
    }



}
