package com.openweatherapp.presenter

import android.util.Log
import com.openweatherapp.delegate.MainActivityDelegate
import com.openweatherapp.model.CurrentLocationWeather
import com.openweatherapp.restclient.RetrofitRxWrapper
import com.openweatherapp.rxbase.BaseServiceRxWrapper
import com.openweatherapp.viewmodel.MainActivityViewModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

internal class MainActivityPresenter(
    private val viewDelegate: MainActivityDelegate,
    private val wrapper: RetrofitRxWrapper) {

    val viewModel: MainActivityViewModel by lazy {
        this.viewDelegate.getViewModel(MainActivityViewModel::class.java)
    }
    private var disposable: Disposable? = null

    fun fetchCurrentLocationWeather(userLocality: String) {
        viewDelegate.hideErrorView()
        viewDelegate.hideCurrentCityWeather()
        viewDelegate.showShimmerAnimation()

        if (viewModel.single == null) {
            viewModel.single = wrapper.fetchCurrentLocationWeather(userLocality)
        }
        disposable?.dispose()

        disposable = this.viewModel.single?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableSingleObserver<CurrentLocationWeather>() {
                override fun onSuccess(response: CurrentLocationWeather) {
                    viewDelegate.hideShimmerAnimation()
                    viewDelegate.showCurrentCityWeather()
                    viewModel.response = response
                    viewModel.single = null
                    viewDelegate.onSuccess(viewModel.response!!)

                }

                override fun onError(e: Throwable) {
                    viewModel.single = null
                    viewDelegate.hideShimmerAnimation()
                    viewDelegate.showErrorView()
                    var message: String = if (e is UnknownHostException) {
                        BaseServiceRxWrapper.NO_INTERNET_CONNECTION
                    } else {
                        e.message.toString()
                    }
                    Log.d("TAG", message)
                    dispose()
                }
            })

    }


}
