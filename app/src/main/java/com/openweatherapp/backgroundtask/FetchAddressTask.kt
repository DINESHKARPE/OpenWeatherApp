package com.openweatherapp.backgroundtask

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import com.openweatherapp.R
import com.openweatherapp.event.OnAddressFetchCompleted
import com.openweatherapp.model.UserLocation
import java.io.IOException
import java.util.*

class FetchAddressTask(
    private val mContext: Context,
    private val mListener: OnAddressFetchCompleted
) : AsyncTask<Location?, Void?, UserLocation>() {

    private val TAG = FetchAddressTask::class.java.simpleName
    private var addresses: List<Address>? = null
    private var resultMessage = ""
    private var userLocation: UserLocation? = null

    protected override fun doInBackground(vararg params: Location?): UserLocation? {
        val geocoder = Geocoder(
            mContext,
            Locale.getDefault()
        )
        val location = params[0]
        userLocation = UserLocation()
        try {
            addresses = geocoder.getFromLocation(
                location!!.latitude,
                location!!.longitude,  // In this sample, get just a nearbyCitysingle address
                1
            )
            if (addresses == null || addresses!!.size == 0) {
                if (resultMessage.isEmpty()) {
                    resultMessage = mContext
                        .getString(R.string.no_address_found)
                    Log.e(TAG, resultMessage)
                }
            } else {
                // If an address is found, read it into resultMessage
                val address = addresses!![0]
                val addressParts =
                    ArrayList<String?>()

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread
                for (i in 0..address.maxAddressLineIndex) {
                    addressParts.add(address.getAddressLine(i))
                }
                userLocation!!.isValidLocation = true
                userLocation!!.countryCode = address.countryCode
                userLocation!!.userAddress = TextUtils.join("\n", addressParts)
                userLocation!!.userLocality = address.locality
                userLocation!!.userSubAdminArea = address.subAdminArea
                userLocation!!.userAdminArea = address.adminArea
            }
        } catch (ioException: IOException) {
            resultMessage = mContext
                .getString(R.string.service_not_available)
            userLocation!!.isValidLocation = false
            Log.e(TAG, resultMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            resultMessage = mContext
                .getString(R.string.invalid_lat_long_used)
            userLocation!!.isValidLocation = false
            Log.e(
                TAG, resultMessage + ". " +
                        "Latitude = " + location!!.latitude +
                        ", Longitude = " +
                        location!!.longitude, illegalArgumentException
            )
        }
        return userLocation!!
    }

    override fun onPostExecute(userLocation: UserLocation) {
        mListener.OnAddressFetchCompleted(userLocation)
        super.onPostExecute(userLocation)
    }


}