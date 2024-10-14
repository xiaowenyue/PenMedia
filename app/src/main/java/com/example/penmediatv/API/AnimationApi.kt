package com.example.penmediatv.API
import com.example.penmediatv.Data.AnimationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimationApi {
    @GET("animation")
    fun getAnimations(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<AnimationResponse>
}
