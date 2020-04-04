package com.openweatherapp.viewmodel


import androidx.lifecycle.ViewModel
import com.openweatherapp.model.CurrentLocationWeather
import io.reactivex.Single

class MainActivityViewModel : ViewModel() {

    var single: Single<CurrentLocationWeather>? = null
    var response: CurrentLocationWeather? = null
}
