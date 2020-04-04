package com.openweatherapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import com.openweatherapp.*
import com.openweatherapp.backgroundtask.FetchAddressTask
import com.openweatherapp.delegate.MainActivityDelegate
import com.openweatherapp.event.OnAddressFetchCompleted
import com.openweatherapp.model.CurrentLocationWeather
import com.openweatherapp.model.UserLocation
import com.openweatherapp.model.WeatherList
import com.openweatherapp.presenter.MainActivityPresenter
import com.openweatherapp.restclient.RetrofitRxWrapper
import com.openweatherapp.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.today_weather.*
import java.util.*

class MainActivity : StandardActivity(), MainActivityDelegate, OnAddressFetchCompleted {


    private lateinit var mainActivityPresenter: MainActivityPresenter
    private var viewModel: MainActivityViewModel? = null

    private var repositoryList: MutableList<WeatherList>? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var preferencesManager: PreferencesManager

    private val mockUrl by lazy {
        intent?.extras?.getString("MOCK_URL", null)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        preferencesManager = PreferencesManager(applicationContext)

        var retrofitRxWrapper =
            RetrofitRxWrapper(applicationContext, mockUrl ?: BuildConfig.REST_API)

        this.mainActivityPresenter =
            MainActivityPresenter(
                this,
                retrofitRxWrapper
            )

        this.viewModel = this.getViewModel(MainActivityViewModel::class.java)


        retry.setOnClickListener {
            preferencesManager.getValue(PreferencesManager.CITY,"")?.let { it1 ->
                this.mainActivityPresenter.fetchCurrentLocationWeather(
                    it1
                )
            }
        }

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        swipeRefreshLayout!!.setOnRefreshListener {
            preferencesManager.getValue(PreferencesManager.CITY,"")?.let {
                this.mainActivityPresenter.fetchCurrentLocationWeather(
                    it
                )
            }
        }


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

        repositoryList = response.weatherList!!.subList(1,response.weatherList!!.size)
        textView.text = Utility.getFriendlyDayString(
            applicationContext,
            Utility.getDbDateString(
                response.weatherList!![0].dt?.times(1000L)?.let { it1 -> Date(it1) })
        )
        textViewDeg.text = applicationContext.getString(R.string.format_temperature, response.weatherList!!.get(0).temp!!.temp_max);
        textcity.text = (response.city as JsonObject).get("name").asString

        Glide.with(applicationContext)
            .load(BuildConfig.IMAGE_API + response.weatherList!![0].weatherDetilsList!![0].icon + ".png")
            .apply(RequestOptions.circleCropTransform())
            .into(image)


        swipeRefreshLayout!!.isRefreshing = false
    }

    override fun hideCurrentCityWeather() {
        todays_weather.visibility = View.GONE
    }

    override fun showCurrentCityWeather() {
        todays_weather.visibility = View.VISIBLE
    }

    private fun loadAddress(location: android.location.Location) {

        val fetchCity = FetchAddressTask(applicationContext, this)
        fetchCity.execute(location)

    }

    override fun OnAddressFetchCompleted(`object`: Any) {
        var userLocation: UserLocation = `object` as UserLocation

        if (userLocation.isValidLocation) {
            userLocation.userLocality?.let {
                this.mainActivityPresenter.fetchCurrentLocationWeather(
                    it
                )
            }
            storeDefaultLocationDataToPref(userLocation)

        } else {
            showMessage(getString(R.string.no_address_found))
        }
    }

    private fun storeDefaultLocationDataToPref(userLocation: UserLocation) {
        if (userLocation.userLocality != null)
            preferencesManager.setKeyValue(PreferencesManager.CITY, userLocation.userLocality)
        if (userLocation.userSubAdminArea != null)
            preferencesManager.setKeyValue(PreferencesManager.DIST, userLocation.userSubAdminArea)
        if (userLocation.userAdminArea != null)
            preferencesManager.setKeyValue(PreferencesManager.STATE, userLocation.userAdminArea)
        if (userLocation.userPostalCode != null)
            preferencesManager.setKeyValue(PreferencesManager.PINCODE, userLocation.userPostalCode)
        if (userLocation.countryCode != null) {
            preferencesManager.setKeyValue(PreferencesManager.COUNTRY, userLocation.countryCode)
        }
    }


    private fun showMessage(message: String) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

}
