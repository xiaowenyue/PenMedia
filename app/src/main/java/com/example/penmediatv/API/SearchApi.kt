package com.example.penmediatv.API

import com.example.penmediatv.Data.SearchRequest
import com.example.penmediatv.Data.SearchResponse
import com.example.penmediatv.Data.TrendRecommendResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SearchApi {
    @GET("video/trending/recommend")
    fun getTrendingRecommend(): Call<TrendRecommendResponse>

    @POST("video/search")
    fun search(
        @Body searchRequest: SearchRequest
    ): Call<SearchResponse>
}