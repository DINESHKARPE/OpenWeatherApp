package com.openweatherapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi

open class OpenWeatherApp : Application() {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onCreate() {
        super.onCreate()
    }


}