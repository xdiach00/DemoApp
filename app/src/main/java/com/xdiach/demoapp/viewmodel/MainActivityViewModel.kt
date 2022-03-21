package com.xdiach.demoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xdiach.demoapp.data.DataModel
import com.xdiach.demoapp.retrofit.RetroInstance
import com.xdiach.demoapp.retrofit.RetroServiceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

class MainActivityViewModel: ViewModel() {

    lateinit var liveDataList: MutableLiveData<List<DataModel>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<DataModel>> {
        return liveDataList
    }

    fun makeAPICall() {
        val retroInstance = RetroInstance.getRetroInstance()
        val retroService = retroInstance.create(RetroServiceInterface::class.java)
        val call = retroService.getDataList()

        GlobalScope.launch(Dispatchers.IO) {
            val dataPost = call.await()

            liveDataList.postValue(dataPost)
        }

        /* Without coroutines:
        * call.enqueue(object : Callback<List<DataModel>> {
            override fun onFailure(call: Call<List<DataModel>>, t: Throwable) {
                liveDataList.postValue(null)
            }

            override fun onResponse(
                call: Call<List<DataModel>>,
                response: Response<List<DataModel>>
            ) {
                liveDataList.postValue(response.body())
            }
        })*/
    }
}