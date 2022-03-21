package com.xdiach.demoapp.data

import java.util.*

data class DataModel(
    val uuid: String?,
    val name: String?,
    val thumbnail_image: String?,
    val location: Location
    )

data class Location(
    val latitude: Double,
    val longitude: Double,
    )
