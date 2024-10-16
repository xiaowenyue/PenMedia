package com.example.penmediatv.API

import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.CollectionAddRequest
import com.example.penmediatv.Data.CollectionAddResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CollectionApi {
    @GET("collection")
    fun getCollection(
        @Query("deviceId") deviceId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @POST("collection/add")
    fun addCollection(
        @Body collectionRequest: CollectionAddRequest
    ): Call<CollectionAddResponse>
}