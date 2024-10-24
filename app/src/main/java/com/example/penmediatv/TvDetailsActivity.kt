package com.example.penmediatv

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.API.CollectionApi
import com.example.penmediatv.API.EpisodeApi
import com.example.penmediatv.API.RecommendationApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.CollectionAddRequest
import com.example.penmediatv.Data.CollectionAddResponse
import com.example.penmediatv.Data.EpisodeResponse
import com.example.penmediatv.Data.ResourceDetailResponse
import com.example.penmediatv.databinding.ActivityTvDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TvDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTvDetailsBinding
    private var isCollected = false  // 用于记录是否收藏
    private lateinit var recommendationAdapter: RelevantRecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val episodesSize = intent.getIntExtra("VIDEO_EPISODE", 1)
        setupRecyclerView()
        fetchResourceDetails()
        binding.btnCollect.setOnClickListener {
            collectVideo()
        }
        // 动态生成剧集范围按钮
        generateEpisodeRangeButtons(episodesSize)
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
                                this@TvDetailsActivity,
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
                                    this@TvDetailsActivity,
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
                            binding.btWatchNow.setOnClickListener {
                                val intent =
                                    Intent(this@TvDetailsActivity, VideoPlayActivity::class.java)
                                intent.putExtra("VIDEO_URL", resourceDetailResponse.videoUrl)
                                intent.putExtra("VIDEO_ID", videoId)
                                startActivity(intent)
                            }
                        } else {
                            Log.e(
                                "MovieDetailsActivity",
                                "resourceDetailResponse is null"
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
            "ANIMATION" -> videoId?.let { recommendationApi.getAnimationRecommendation(it) }
            "DOCUMENTARY" -> videoId?.let { recommendationApi.getDocumentaryRecommendation(it) }
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
                            "data is null"
                        )
                    }
                } else {
                    Log.e(
                        "MovieDetailsActivity",
                        "Failed to fetch relevant recommendations: ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<AnimationResponse>, t: Throwable) {
                // 处理失败响应
                Log.e(
                    "MovieDetailsActivity",
                    "Network error, failed to fetch relevant recommendations: ${t.message}"
                )
            }
        })
    }

    // 动态生成剧集范围按钮 (1-10, 11-20,...)
    private fun generateEpisodeRangeButtons(totalEpisodes: Int) {
        val rangesContainer = binding.llEpisodeRanges
        rangesContainer.removeAllViews() // 清空已有的范围按钮
        // 每个范围 10 集
        val numRanges = (totalEpisodes + 9) / 10
        for (i in 0 until numRanges) {
            val start = i * 10 + 1
            val end = minOf((i + 1) * 10, totalEpisodes)
            val rangeButton = AppCompatButton(
                ContextThemeWrapper(this, R.style.EpisodeRangeButton),
                null,
                R.style.EpisodeRangeButton
            ).apply {
                text = "$start-$end"
                setOnClickListener {
                    updateEpisodes(start, end)
                }
            }
            // 设置按钮的布局参数，增加间距
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0) // 设置按钮的上下左右间距
            rangeButton.layoutParams = layoutParams
            rangesContainer.addView(rangeButton)

            // 如果是第一个范围按钮，默认展示对应的剧集
            if (i == 0) {
                updateEpisodes(start, end) // 默认展示第一个范围的剧集
            }
        }
    }

    // 动态生成具体剧集按钮
    private fun updateEpisodes(start: Int, end: Int) {
        val episodesContainer = binding.llEpisodes
        episodesContainer.removeAllViews() // Clear existing episode buttons
        for (i in start..end) {
            // Create individual episode buttons
            val episodeButton = AppCompatButton(
                ContextThemeWrapper(this, R.style.EpisodeButton),
                null,
                R.style.EpisodeButton
            ).apply {
                text = i.toString()
                setOnClickListener {
                    fetchEpisodePlayUrl(i) // Fetch play URL for the selected episode
                }
            }
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0) // Set margins for the buttons
            episodeButton.layoutParams = layoutParams
            episodesContainer.addView(episodeButton)
        }
    }

    private fun fetchEpisodePlayUrl(episodeNumber: Int) {
        val videoId = intent.getStringExtra("VIDEO_ID") ?: return
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val resourceApi = retrofit.create(EpisodeApi::class.java)

        // Assume each page contains 10 episodes
        val page = (episodeNumber + 9) / 10 // Calculate the page number dynamically
        val pageSize = 10 // Fixed page size for each request

        // Call the API to fetch the episode details
        val call = resourceApi.getEpisode(videoId, page, pageSize)
        call.enqueue(object : Callback<EpisodeResponse> {
            override fun onResponse(call: Call<EpisodeResponse>, response: Response<EpisodeResponse>) {
                if (response.isSuccessful) {
                    val episodeList = response.body()?.data?.records
                    if (episodeList != null) {
                        val episodeIndex = (episodeNumber - 1) % 10 // Index of the episode on the page
                        val playUrl = episodeList.getOrNull(episodeIndex)?.videoUrl
                        if (playUrl != null) {
                            val intent = Intent(this@TvDetailsActivity, VideoPlayActivity::class.java)
                            intent.putExtra("VIDEO_URL", playUrl)
                            startActivity(intent)
                        } else {
                            Log.e("TvDetailsActivity", "No play URL found for episode $episodeNumber")
                        }
                    }
                } else {
                    Log.e("TvDetailsActivity", "Failed to fetch episode details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EpisodeResponse>, t: Throwable) {
                Log.e("TvDetailsActivity", "Error fetching episode details: ${t.message}")
            }
        })
    }
}