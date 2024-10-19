package com.example.penmediatv

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.API.InfoApi
import com.example.penmediatv.Data.InfoResponse
import com.example.penmediatv.databinding.ActivitySplashBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.Retrofit.*
import retrofit2.converter.gson.GsonConverterFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var fetchedTitle: String? = null
    private var fetchedContent: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置渐入渐出动画
        val fadeInOutAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1000 // 持续时间
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }
        binding.startingText.startAnimation(fadeInOutAnimation)
        fetchData()
        // 模拟进度条加载
        simulateLoading()
    }

    private fun simulateLoading() {
        // 这里模拟进度条加载，您可以根据实际需求加载数据
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.progress = 25
        }, 750)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.progress = 50
        }, 1500)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.progress = 75
        }, 2250)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.progress = 100
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("title", fetchedTitle)
                putExtra("content", fetchedContent)
            }
            startActivity(intent)
            finish()
        }, 3000)
    }

    private val retrofit = Builder()
        .baseUrl("http://44.208.55.69")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(InfoApi::class.java)
    private fun fetchData() {
        apiService.getInfo().enqueue(object : Callback<InfoResponse> {
            override fun onResponse(call: Call<InfoResponse>, response: Response<InfoResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        if (apiResponse.code == "0000") {
                            fetchedTitle = apiResponse.data[0].content
                            fetchedContent = apiResponse.data[0].contentTitle
                        }
                    }
                }
            }

            override fun onFailure(call: Call<InfoResponse>, t: Throwable) {
                // 处理错误
                fetchedTitle = null
                fetchedContent = null
            }
        })
    }
}
