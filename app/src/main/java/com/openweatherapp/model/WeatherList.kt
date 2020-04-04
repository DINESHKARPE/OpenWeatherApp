package com.openweatherapp.model

import com.google.gson.annotations.SerializedName


class WeatherList  {

    @SerializedName("dt")
    var dt: Long? = 0


    @SerializedName("pressure")
    var pressure: String? = ""

    @SerializedName("humidity")
    var humidity: String? = ""


    @SerializedName("speed")
    var  speed:String? = ""

    @SerializedName("sunrise")
    var sunrise:String? = ""

    @SerializedName("sunset")
    var sunset: String? = ""


    @SerializedName("deg")
    var deg: Double? = 0.0

    @SerializedName("temp")
    var temp: Temperature? = null

    @SerializedName("weather")
    var weatherDetilsList: MutableList<WeatherDetils>? = null

}



