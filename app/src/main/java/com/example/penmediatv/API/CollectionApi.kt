package com.example.penmediatv.API

import com.example.penmediatv.Data.CollectionAddRequest
import com.example.penmediatv.Data.CollectionAddResponse
import com.example.penmediatv.Data.CollectionClearRequest
import com.example.penmediatv.Data.CollectionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CollectionApi {
    @GET("video/collection")
    fun getCollection(
        @Query("deviceId") deviceId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<CollectionResponse>

    @POST("video/collection/add")
    fun addCollection(
        @Body collectionRequest: CollectionAddRequest
    ): Call<CollectionAddResponse>

    @POST("video/collection/clear")
    fun clearCollection(
        @Body clearResponse: CollectionClearRequest
    ): Call<CollectionResponse>

    @POST("video/collection/clear/single")
    fun clearSingleCollection(
        @Body clearSingleRequest: CollectionAddRequest
    ): Call<CollectionAddResponse>
}