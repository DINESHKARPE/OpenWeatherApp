package com.openweatherapp.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.json.JSONObject


class CurrentLocationWeather  {

    @SerializedName("list")
    var weatherList: MutableList<WeatherList>? = null

    @SerializedName("city")
    var city: JsonElement? = null

}



