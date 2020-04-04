package com.openweatherapp.model

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject


class Temperature  {

    @SerializedName("day")
    var day: Double  = 0.0

    @SerializedName("min")
    var temp_min: Double  = 0.0

    @SerializedName("max")
    var temp_max: Double  = 0.0

    @SerializedName("eve")
    var temp_eve: Double  = 0.0

    @SerializedName("morn")
    var temp_morn: Double  = 0.0

}



