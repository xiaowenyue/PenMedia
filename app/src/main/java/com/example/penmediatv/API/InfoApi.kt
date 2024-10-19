package com.example.penmediatv.API

import com.example.penmediatv.Data.InfoResponse
import retrofit2.Call
import retrofit2.http.GET

interface InfoApi {
    @GET("info/list")
    fun getInfo(): Call<InfoResponse>
}