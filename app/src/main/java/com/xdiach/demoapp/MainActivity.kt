package com.xdiach.demoapp

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.xdiach.demoapp.adapter.DataListAdapter
import com.xdiach.demoapp.viewmodel.MainActivityViewModel

var longitude: Double = 50.100558
var latitude: Double = 14.424798

class MainActivity : AppCompatActivity() {

    var sortTypeABC:Boolean = true
    lateinit var recyclerAdapter: DataListAdapter
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

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
                    sortedData = sortedData.sortedWith(compareBy({ getDist(it.location.latitude, it.location.longitude) }))
                }

                recyclerAdapter.setDataList(sortedData)
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        viewModel.makeAPICall()
    }

    fun getDist(latitudeTarget: Double, longitudeTarget: Double): Int {
        val startPoint = Location("locationA")
        startPoint.setLatitude(latitude)
        startPoint.setLongitude(longitude)

        val endPoint = Location("locationA")
        endPoint.setLatitude(latitudeTarget)
        endPoint.setLongitude(longitudeTarget)

        var distance = startPoint.distanceTo(endPoint)
        distance /= 100

        return distance.toInt()
    }

    private fun checkLocationPermission() {
        val loc = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }

        loc.addOnSuccessListener {
            if (it != null) {
                latitude = it.latitude
                longitude = it.longitude
            }
        }
    }
}