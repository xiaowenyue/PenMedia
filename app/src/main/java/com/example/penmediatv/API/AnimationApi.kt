package com.example.penmediatv.API
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.SwiperResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimationApi {
    @GET("animation")
    fun getAnimations(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("animation/swiper")
    fun getSwiperAnimations(): Call<SwiperResponse>

    @GET("movie")
    fun getMovies(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("documentary")
    fun getDocumentary(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>

    @GET("tv")
    fun getTv(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>
}
