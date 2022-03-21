package com.xdiach.demoapp.retrofit

import com.xdiach.demoapp.data.DataModel
import com.xdiach.demoapp.data.ObjectModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface RetroServiceInterface {

    @GET("places")
    fun getDataList(): Call<List<DataModel>>

    @GET("places/{uuid}")
    fun getDataObject(@Path("uuid") uuid: String): Call<ObjectModel>

}