package com.openweatherapp.event

import android.location.Location

interface PositionListener {
    fun onPositionUpdate(position: Location)
}