package com.openweatherapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openweatherapp.R
import com.openweatherapp.adapter.MainActivityListAdapter
import com.openweatherapp.model.WeatherList

class BottomSheetFragment(
    private val mContext: Context,
    private val repositoryList: MutableList<WeatherList>?
) : BottomSheetDialogFragment() {


    private var mainActivityListAdapter: MainActivityListAdapter? = null
    private var repository_list1: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var  view = inflater.inflate(R.layout.bottom_sheet, container, false)


        repository_list1 = view.findViewById(R.id.repository_list1);

        this.mainActivityListAdapter = mContext?.let { MainActivityListAdapter(repositoryList, it) }

        repository_list1!!.layoutManager = LinearLayoutManager(mContext)
        repository_list1!!.adapter = mainActivityListAdapter
        return view;
    }
}