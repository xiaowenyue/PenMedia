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
import com.example.penmediatv.API.CollectionApi
import com.example.penmediatv.Data.CollectionAddRequest
import com.example.penmediatv.Data.CollectionAddResponse
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

        binding.btWatchNow.setOnClickListener {
            Toast.makeText(this, "Watch Now", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VideoPlayActivity::class.java)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = RelevantRecommendationAdapter(getMovies())
        binding.btnCollect.setOnClickListener {
            val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
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
            // 创建请求体
            val collectionRequest = CollectionAddRequest(
                deviceId = androidId,
                videoId = "2024-10-13-20-28-28"  // 根据实际情况设置视频ID
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