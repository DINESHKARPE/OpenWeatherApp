package com.openweatherapp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.openweatherapp.event.PositionListener
import com.openweatherapp.position.CurrentLocationCallBack
import com.openweatherapp.position.WeatherAppFusedLocationProvider
import com.openweatherapp.receiver.ConnectivityReceiver
import java.util.*


abstract class StandardActivity : AppCompatActivity(), PositionListener,
    ConnectivityReceiver.ConnectivityReceiverListener {


    private lateinit var locationCallBack: CurrentLocationCallBack
    lateinit var weatherAppFusedLocationProvider: WeatherAppFusedLocationProvider
    private lateinit var myReceiver: LocationReceiver

    private var isConnected: Boolean = false
    private var receiver: ConnectivityReceiver? = null


    lateinit var networkCallback: ConnectivityManager.NetworkCallback

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = ConnectivityManager.NetworkCallback()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()

        receiver = ConnectivityReceiver()
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


        locationCallBack = CurrentLocationCallBack(applicationContext, this)
        weatherAppFusedLocationProvider = applicationContext?.let { WeatherAppFusedLocationProvider(this, locationCallBack) }!!

        myReceiver = LocationReceiver()

        weatherAppFusedLocationProvider.checkLocationSettings()

    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this


        if (weatherAppFusedLocationProvider != null) {
            weatherAppFusedLocationProvider.startUpdates()
        }
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(myReceiver,
            IntentFilter(CurrentLocationCallBack.ACTION_BROADCAST))
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        this.isConnected = isConnected
    }

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T {
        return ViewModelProviders.of(this).get(clazz)
    }

    open fun isConnectedToInternet(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.activeNetworkInfo ?: return false
        return true
    }

    inner class LocationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action) {
                 broadcast -> {
                    val location = intent.getParcelableExtra<Location>(CurrentLocationCallBack.EXTRA_LOCATION)
                    if (location != null) {
                        weatherAppFusedLocationProvider.stopUpdate()
                        onPositionUpdate(location)
                    }
                }
            }
        }
    }

    private fun checkPermission() {
        val missingPermissions: MutableSet<String> = HashSet()
        if (ContextCompat.checkSelfPermission(applicationContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (missingPermissions.isEmpty()) {
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(missingPermissions.toTypedArray(),
                    prrmiddtionrequestlocation)
            }
        }
    }

    companion object{
        const val  broadcast = "com.openweatherapp.position.CurrentLocationCallBack.broadcast"
        const val  prrmiddtionrequestlocation = 2
    }
}
