package com.openweatherapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.openweatherapp.*
import com.openweatherapp.delegate.MainActivityDelegate
import com.openweatherapp.event.OnAddressFetchCompleted
import com.openweatherapp.model.CurrentLocationWeather
import com.openweatherapp.model.UserLocation
import com.openweatherapp.model.WeatherList
import com.openweatherapp.presenter.MainActivityPresenter
import com.openweatherapp.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.error_layout.*

class MainActivity : StandardActivity(), MainActivityDelegate, OnAddressFetchCompleted {


    private lateinit var mainActivityPresenter: MainActivityPresenter
    private var viewModel: MainActivityViewModel? = null

    private var repositoryList: MutableList<WeatherList>? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var preferencesManager: PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPositionUpdate(position: android.location.Location) {
        if (position.hasAccuracy()) {

            loadAddress(position)
        }
    }

    override fun showShimmerAnimation() {
        shimmer_view_container.visibility = View.VISIBLE
    }

    override fun hideShimmerAnimation() {
        shimmer_view_container.visibility = View.GONE
    }

    override fun showErrorView() {
        error_layout.visibility = View.VISIBLE
    }

    override fun hideErrorView() {
        error_layout.visibility = View.GONE
    }

    override fun onSuccess(response: CurrentLocationWeather) {


    }

    override fun hideCurrentCityWeather() {
        todays_weather.visibility = View.GONE
    }

    override fun showCurrentCityWeather() {
        todays_weather.visibility = View.VISIBLE
    }

    private fun loadAddress(location: android.location.Location) {


    }

    override fun OnAddressFetchCompleted(`object`: Any) {

    }

    private fun storeDefaultLocationDataToPref(userLocation: UserLocation) {

    }


    private fun showMessage(message: String) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

}
