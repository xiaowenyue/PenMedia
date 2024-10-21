package com.example.penmediatv

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.ActivityMovieDetailsBinding
import android.provider.Settings
import android.util.Log
import com.bumptech.glide.Glide
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.API.CollectionApi
import com.example.penmediatv.API.RecommendationApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.CollectionAddRequest
import com.example.penmediatv.Data.CollectionAddResponse
import com.example.penmediatv.Data.ResourceDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private var isCollected = false  // 用于记录是否收藏
    private lateinit var recommendationAdapter: RelevantRecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        fetchResourceDetails()
        val videoId = intent.getStringExtra("VIDEO_ID")
        binding.btWatchNow.setOnClickListener {
            val intent = Intent(this, VideoPlayActivity::class.java)
            intent.putExtra("VIDEO_ID", videoId)
            startActivity(intent)
        }

        binding.btnCollect.setOnClickListener {
            collectVideo()
        }
    }

    private fun collectVideo() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val videoId = intent.getStringExtra("VIDEO_ID")
        Log.v("MovieDetailsActivity", "Android ID: $androidId")
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_collected)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        // 根据是否已经收藏，设置弹窗内容
        val imageView = dialog.findViewById<ImageView>(R.id.iv_connected)
        val titleTextView = dialog.findViewById<TextView>(R.id.tv_title)
        val subTextView = dialog.findViewById<TextView>(R.id.tv_content)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val collectionApi = retrofit.create(CollectionApi::class.java)
        if (videoId != null) {
            // 创建请求体
            val collectionRequest = CollectionAddRequest(
                deviceId = androidId,
                videoId = videoId
            )
            // 调用接口
            val call = collectionApi.addCollection(collectionRequest)

            call.enqueue(object : Callback<CollectionAddResponse> {
                override fun onResponse(
                    call: Call<CollectionAddResponse>,
                    response: Response<CollectionAddResponse>
                ) {
                    if (response.isSuccessful) {
                        val collectionResponse = response.body()
                        if (collectionResponse?.code == "0000") {
                            // 收藏成功，更新UI
                            imageView.setImageResource(R.drawable.ic_connected)
                            titleTextView.text = "Collected"
                            subTextView.text = "View at the personal center"
                            isCollected = true  // 更新收藏状态为已收藏
                            ContextCompat.getDrawable(
                                this@MovieDetailsActivity,
                                R.drawable.ic_connect
                            )?.let {
                                binding.btnCollect.setCompoundDrawablesWithIntrinsicBounds(
                                    it,
                                    null,
                                    null,
                                    null
                                )
                            }
                        } else {
                            // 收藏失败，显示失败提示
                            imageView.setImageResource(R.drawable.ic_unconnected)
                            titleTextView.text = "Uncollected"
                            subTextView.visibility = TextView.GONE
                            isCollected = false
                        }
                    } else {
                        // 处理失败响应
                        imageView.setImageResource(R.drawable.ic_unconnected)
                        titleTextView.text = "Uncollected"
                        subTextView.visibility = TextView.GONE
                        isCollected = false
                    }
                    dialog.show()

                    // 使用 Handler 使弹窗在3秒后自动消失
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 2000)
                }

                override fun onFailure(call: Call<CollectionAddResponse>, t: Throwable) {
                    // 请求失败处理
                    Log.e("MovieDetailsActivity", "Failed to collect: ${t.message}")
                    imageView.setImageResource(R.drawable.ic_unconnected)
                    titleTextView.text = "Uncollected"
                    subTextView.visibility = TextView.GONE
                    isCollected = false
                    dialog.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 2000)
                }
            })
        } else {
            Log.e("MovieDetailsActivity", "Failed to collect: videoId is null")
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        recommendationAdapter = RelevantRecommendationAdapter(mutableListOf())
        binding.recyclerView.adapter = recommendationAdapter
    }

    private fun fetchResourceDetails() {
        val videoId = intent.getStringExtra("VIDEO_ID")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val resourceApi = retrofit.create(AnimationApi::class.java)
        if (videoId != null) {
            val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            val call = resourceApi.getResourceDetail(videoId, androidId)
            call.enqueue(object : Callback<ResourceDetailResponse> {
                override fun onResponse(
                    call: Call<ResourceDetailResponse>,
                    response: Response<ResourceDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        val resourceDetailResponse = response.body()?.data
                        if (resourceDetailResponse != null) {
                            binding.tvMovieTitle.text = resourceDetailResponse.videoNameEn
                            binding.tvMovieIntroductionContent.text =
                                resourceDetailResponse.introduction
                            binding.tvMovieGenre.text = resourceDetailResponse.category
                            binding.tvMovieRegion.text = resourceDetailResponse.otherInfo.region
                            binding.tvMovieDirector.text = resourceDetailResponse.otherInfo.director
                            if (resourceDetailResponse.collection) {
                                ContextCompat.getDrawable(
                                    this@MovieDetailsActivity,
                                    R.drawable.ic_connect
                                )?.let {
                                    binding.btnCollect.setCompoundDrawablesWithIntrinsicBounds(
                                        it,
                                        null,
                                        null,
                                        null
                                    )
                                }
                            }
                            Glide.with(binding.root)
                                .load(resourceDetailResponse.videoCover)
                                .into(binding.moviePoster)
                            fetchRecommendList(resourceDetailResponse.videoType)
                        } else {
                            Log.e(
                                "MovieDetailsActivity",
                                "resourceDetailResponse is null"
                            )
                        }
                    } else {
                        // 根据不同的状态码进行处理
                        when (response.code()) {
                            404 -> {
                                // 资源未找到
                                Log.e("MovieDetailsActivity", "资源未找到 (404): ${response.message()}")
                                Toast.makeText(this@MovieDetailsActivity, "资源未找到", Toast.LENGTH_SHORT).show()
                            }
                            403 -> {
                                // 权限不足
                                Log.e("MovieDetailsActivity", "权限不足 (403): ${response.message()}")
                                Toast.makeText(this@MovieDetailsActivity, "您没有访问该资源的权限", Toast.LENGTH_SHORT).show()
                            }
                            401 -> {
                                // 未授权
                                Log.e("MovieDetailsActivity", "未授权 (401): ${response.message()}")
                                Toast.makeText(this@MovieDetailsActivity, "请先登录", Toast.LENGTH_SHORT).show()
                            }
                            500 -> {
                                // 服务器错误
                                Log.e("MovieDetailsActivity", "服务器错误 (500): ${response.message()}")
                                val dialog = Dialog(this@MovieDetailsActivity)
                                dialog.setContentView(R.layout.dialog_network_disconnect)
                                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                dialog.show()

                                Handler(Looper.getMainLooper()).postDelayed({
                                    dialog.dismiss()
                                }, 2000)
                            }
                            else -> {
                                // 其他错误
                                Log.e("MovieDetailsActivity", "未知错误: ${response.code()}, ${response.message()}")
                                val dialog = Dialog(this@MovieDetailsActivity)
                                dialog.setContentView(R.layout.dialog_network_disconnect)
                                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                dialog.show()

                                Handler(Looper.getMainLooper()).postDelayed({
                                    dialog.dismiss()
                                }, 2000)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResourceDetailResponse>, t: Throwable) {
                    Log.e("MovieDetailsActivity", "fetchResourceDetails onFailure: ${t.message}")
                }
            })
        }
    }

    private fun fetchRecommendList(videoType: String) {
        val videoId = intent.getStringExtra("VIDEO_ID")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val recommendationApi = retrofit.create(RecommendationApi::class.java)
        val call: Call<AnimationResponse>? = when (videoType) {
            "MOVIE" -> videoId?.let { recommendationApi.getMovieRecommendation(it) }
            "TV_SERIES" -> videoId?.let { recommendationApi.getTvRecommendation(it) }
            else -> null
        }
        call?.enqueue(object : Callback<AnimationResponse> {
            override fun onResponse(
                call: Call<AnimationResponse>,
                response: Response<AnimationResponse>
            ) {
                if (response.isSuccessful) {
                    val animationResponse = response.body()
                    Log.d("MovieDetailsActivity", "Response Body: $animationResponse")  // 添加这一行
                    val animationData = animationResponse?.data
                    if (animationData != null) {
                        val relevantRecommendations = animationData.records
                        if (relevantRecommendations.isNotEmpty()) {
                            recommendationAdapter.updateMovies(relevantRecommendations)
                        } else {
                            Log.e("MovieDetailsActivity", "No relevant recommendations found")
                        }
                    } else {
                        Log.e(
                            "MovieDetailsActivity",
                            "animationResponse.data is null"
                        )
                    }
                } else {
                    Log.e(
                        "MovieDetailsActivity",
                        "response failed: ${response.message()}"
                    )
                    val dialog = Dialog(this@MovieDetailsActivity)
                    dialog.setContentView(R.layout.dialog_network_disconnect)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 2000)
                }
            }

            override fun onFailure(call: Call<AnimationResponse>, t: Throwable) {
                // 处理失败响应
                Log.e(
                    "MovieDetailsActivity",
                    "fetchRecommendList onFailure: ${t.message}"
                )
            }
        })
    }
}