package com.xdiach.demoapp

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xdiach.demoapp.adapter.DataListAdapter
import com.xdiach.demoapp.adapter.distance
import com.xdiach.demoapp.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    var sortTypeABC:Boolean = true
    lateinit var recyclerAdapter: DataListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preference = SharedPreference(this)
        sortTypeABC = preference.getSortType()

        setSupportActionBar(findViewById(R.id.toolbar))
        val imageSort = findViewById<ImageView>(R.id.imageSort)

        if (sortTypeABC == true) {
            imageSort.setImageResource(R.drawable.ic_dist)
        } else {
            imageSort.setImageResource(R.drawable.ic_abc)
        }

        imageSort.setOnClickListener {
            if (sortTypeABC == true) {
                imageSort.setImageResource(R.drawable.ic_abc)
                sortTypeABC = false
                initViewModel()
            } else {
                imageSort.setImageResource(R.drawable.ic_dist)
                sortTypeABC = true
                initViewModel()
            }
            preference.setSortType(sortTypeABC)
        }

        initRecyclerView()
        initViewModel()
    }


    private fun initRecyclerView() {
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this)
        recyclerAdapter = DataListAdapter()
        findViewById<RecyclerView>(R.id.recyclerView).adapter = recyclerAdapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.getLiveDataObserver().observe(this, Observer {
            if(it != null) {

                val responseData = it
                var sortedData = responseData

                if (sortTypeABC == true) {
                    sortedData = sortedData.sortedWith(compareBy({it.name}))
                } else {
                    sortedData = sortedData.sortedWith(compareBy({ distance }))
                }

                recyclerAdapter.setDataList(sortedData)
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        viewModel.makeAPICall()
    }
}