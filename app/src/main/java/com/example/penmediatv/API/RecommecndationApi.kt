package com.example.penmediatv.API

import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.CollectionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationApi {
    @GET("tv/recommend")
    fun getTvRecommendation(
        @Query("videoId") videoId: String
    ): Call<AnimationResponse>

    @GET("movie/recommend")
    fun getMovieRecommendation(
        @Query("videoId") videoId: String
    ): Call<AnimationResponse>
}