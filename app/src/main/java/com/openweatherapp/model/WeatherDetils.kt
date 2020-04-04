package com.openweatherapp.model

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject


class WeatherDetils  {

    @SerializedName("id")
    var id: String? = ""

    @SerializedName("main")
    var main: String? = ""

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("icon")
    var icon: String? = ""


}



