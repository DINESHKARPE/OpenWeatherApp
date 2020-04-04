package com.openweatherapp.rxbase


import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.CacheControl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


abstract class BaseServiceRxWrapper(
    private val mContext: Context,
    apiEndPoint: String
) {

    protected var retrofit: Retrofit

    private val tag = "RetrofitManager"
    private val headerCacheControl = "Cache-Control"
    private val headerPragma = "Pragma"
    var mCache: Cache? = null

    private var httpClient: OkHttpClient? = null

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
    }
    init {


        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


        httpClient = OkHttpClient.Builder()
            .cache(provideCache())
            .addInterceptor(provideOfflineCacheInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(apiEndPoint)
            .client(this.httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(this.createGson()))

        this.retrofit = retrofitBuilder.build()
    }


    private fun createGson(): Gson {

        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }


    private fun provideCache(): Cache? {
        if (mCache == null) {
            try {
                mCache = Cache(
                    File(mContext.cacheDir, "http-cache"),
                    10 * 1024 * 1024
                ) // 10 MB
            } catch (e: Exception) {
                Log.e(tag, "Could not create Cache!")
            }
        }
        return mCache
    }

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            var request = chain.request()
            if (!isConnected) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(2, TimeUnit.HOURS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(headerPragma)
                    .removeHeader(headerCacheControl)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    private val isConnected: Boolean
        private get() {
            try {
                val e = mContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE
                ) as ConnectivityManager
                val activeNetwork = e.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting
            } catch (e: Exception) {
                Log.w(tag, e.toString())
            }
            return false
        }


}
