package com.openweatherapp.adapter.holder


import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openweatherapp.R
import com.bumptech.glide.request.RequestOptions
import com.openweatherapp.BuildConfig
import com.openweatherapp.Utility.getDbDateString
import com.openweatherapp.Utility.getFriendlyDayString
import com.openweatherapp.model.WeatherList
import java.util.*


class MainActivityViewHolder(
    rootView: View,
    private val mContext: Context
) : RecyclerView.ViewHolder(rootView) {


    private val nextday: AppCompatTextView = rootView.findViewById(R.id.nextday)
    private val nextdaydesc: AppCompatTextView = rootView.findViewById(R.id.nextdaydesc)
    private val image : AppCompatImageView = rootView.findViewById(R.id.nextimageView2)
    private val nextdaymax: AppCompatTextView = rootView.findViewById(R.id.nextdaymax);
    private val nextdaymin: AppCompatTextView = rootView.findViewById(R.id.nextdaymin);


    fun bind(item: WeatherList) {


        nextdaymax.text = item.temp!!.temp_max.toString()
        nextdaymin.text = item.temp!!.temp_min.toString()

        nextday.text = getFriendlyDayString(mContext,getDbDateString(item.dt?.times(1000L)?.let { it1 -> Date(it1) }))
        nextdaydesc.text = item.weatherDetilsList!![0]!!.description

        Glide.with(mContext)
            .load(BuildConfig.IMAGE_API +  item.weatherDetilsList!![0].icon + ".png")
            .apply(RequestOptions.circleCropTransform())
            .into(image)




    }
}
