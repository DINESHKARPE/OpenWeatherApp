package com.openweatherapp.position


import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class WeatherAppFusedLocationProvider(context: Activity, locationCallBack: CurrentLocationCallBack) {

    private val context: Activity = context
    private val fusedLocationProvider: FusedLocationProviderClient? = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallBack: CurrentLocationCallBack = locationCallBack

    private val REQUEST_CHECK_SETTINGS = 0x1

    fun startUpdates() {
        try {
            fusedLocationProvider!!.requestLocationUpdates(fetchLocationRequest(), locationCallBack, Looper.myLooper())
        } catch (e: RuntimeException) {
        }
    }

    fun stopUpdate() {
        fusedLocationProvider?.removeLocationUpdates(locationCallBack)
    }

    fun checkLocationSettings() {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(fetchLocationRequest()).build()
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(settingsRequest)
        task.addOnSuccessListener(context) { locationSettingsResponse ->
            if (locationSettingsResponse.locationSettingsStates.isLocationUsable) {
            }
        }
        task.addOnFailureListener(context) { e ->
            val statusCode = (e as ApiException).statusCode
            if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                try {
                    val resolvable = e as ResolvableApiException
                    resolvable.startResolutionForResult(context,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: SendIntentException) { // Ignore the error
                }
            }
        }
    }

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        fun fetchLocationRequest(): LocationRequest {
            val mLocationRequest = LocationRequest()
            mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
            mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return mLocationRequest
        }
    }

}