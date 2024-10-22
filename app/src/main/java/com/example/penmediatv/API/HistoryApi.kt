package com.example.penmediatv.API

import com.example.penmediatv.Data.HistoryAddRequest
import com.example.penmediatv.Data.HistoryAddResponse
import com.example.penmediatv.Data.HistoryClearRequest
import com.example.penmediatv.Data.HistoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HistoryApi {
    @GET("video/history")
    fun getHistory(
        @Query("deviceId") deviceId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<HistoryResponse>

    @POST("video/history/add")
    fun addHistory(
        @Body historyRequest: HistoryAddRequest
    ): Call<HistoryAddResponse>

    @POST("video/history/clear")
    fun clearHistory(
        @Body clearResponse: HistoryClearRequest
    ): Call<HistoryAddResponse>
}