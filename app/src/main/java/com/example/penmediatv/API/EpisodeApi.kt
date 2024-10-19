package com.example.penmediatv.API

import com.example.penmediatv.Data.EpisodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeApi {
    @GET("episode/detail")
    fun getEpisode(
        @Query("videoId") videoId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<EpisodeResponse>
}