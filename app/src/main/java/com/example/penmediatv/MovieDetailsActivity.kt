package com.example.penmediatv

import android.app.Dialog
import android.content.Intent
import android.os.Build
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchResourceDetails()
        val videoId = intent.getStringExtra("VIDEO_ID")

        binding.btWatchNow.setOnClickListener {
            Toast.makeText(this, "Watch Now", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VideoPlayActivity::class.java)
            intent.putExtra("VIDEO_ID", videoId)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = RelevantRecommendationAdapter(getMovies())
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

    private fun fetchResourceDetails() {
        val videoId = intent.getStringExtra("VIDEO_ID")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val resourceApi = retrofit.create(AnimationApi::class.java)
        if (videoId != null) {
            val call = resourceApi.getResourceDetail(videoId)
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
                            Glide.with(binding.root)
                                .load(resourceDetailResponse.videoCover)
                                .into(binding.moviePoster)
                        } else {
                            Log.e(
                                "MovieDetailsActivity",
                                "Failed to fetch resource details: resourceDetailResponse is null"
                            )
                        }
                    } else {
                        Log.e(
                            "MovieDetailsActivity",
                            "Failed to fetch resource details: ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ResourceDetailResponse>, t: Throwable) {
                    Log.e("MovieDetailsActivity", "Failed to fetch resource details: ${t.message}")
                }
            })
        }
    }

    private fun getMovies(): List<Movie> {
        // Generate dummy movie data
        return listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.ic_search),
            Movie("Movie 3", R.drawable.ic_history),
            Movie("Movie 4", R.drawable.ic_mine),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
            Movie("Movie 7", R.drawable.ic_mine),
            Movie("Movie 8", R.drawable.ic_search)
        )
    }
}