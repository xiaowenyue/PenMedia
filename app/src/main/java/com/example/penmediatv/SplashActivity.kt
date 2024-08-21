package com.example.penmediatv

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置渐入渐出动画
        val fadeInOutAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 1000 // 持续时间
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }
        binding.startingText.startAnimation(fadeInOutAnimation)

        // 模拟进度条加载
        simulateLoading()
    }

    private fun simulateLoading() {
        // 这里模拟进度条加载，您可以根据实际需求加载数据
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.progress = 50
        }, 1500)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.progress = 100
            // 跳转到主页面
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}
