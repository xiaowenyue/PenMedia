package com.example.penmediatv.API
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.HomeResponse
import com.example.penmediatv.Data.ResourceDetailResponse
import com.example.penmediatv.Data.SwiperResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimationApi {
    @GET("video/animation")
    fun getAnimations(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("video/animation/swiper")
    fun getSwiperAnimations(): Call<SwiperResponse>

    @GET("video/movie")
    fun getMovies(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("video/movie/swiper")
    fun getSwiperMovie(): Call<SwiperResponse>

    @GET("video/documentary")
    fun getDocumentary(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("video/documentary/swiper")
    fun getSwiperDocumentary(): Call<SwiperResponse>

    @GET("video/tv")
    fun getTv(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("video/tv/swiper")
    fun getSwiperTv(): Call<SwiperResponse>

    @GET("video/resource/detail")
    fun getResourceDetail(
        @Query("videoId") videoId: String,
        @Query("deviceId") deviceId: String
    ): Call<ResourceDetailResponse>

    @GET("video/home/resource")
    fun getHomeResource(): Call<HomeResponse>
}
