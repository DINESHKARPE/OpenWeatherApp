package com.openweatherapp.delegate

import androidx.lifecycle.ViewModel
import com.openweatherapp.model.CurrentLocationWeather

internal interface MainActivityDelegate {

    fun showShimmerAnimation()
    fun hideShimmerAnimation()

    fun showErrorView()
    fun hideErrorView()
    fun isConnectedToInternet(): Boolean
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T
    fun onSuccess(response: CurrentLocationWeather)
    fun hideCurrentCityWeather()
    fun showCurrentCityWeather()
}
