package com.example.penmediatv.API

import com.example.penmediatv.Data.TrendRecommendResponse
import retrofit2.Call
import retrofit2.http.GET

interface SearchApi {
    @GET("video/trending/recommend")
    fun getTrendingRecommend(): Call<TrendRecommendResponse>
}