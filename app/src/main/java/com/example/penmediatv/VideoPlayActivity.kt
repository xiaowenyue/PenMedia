package com.example.penmediatv

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.penmediatv.API.HistoryApi
import com.example.penmediatv.Data.HistoryAddRequest
import com.example.penmediatv.Data.HistoryAddResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoPlayActivity : AppCompatActivity() {
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        playerView = findViewById(R.id.player_view)
        initializePlayer()
    }

    private fun initializePlayer() {
        // 初始化 ExoPlayer
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        // 播放 Bunny.net 的 m3u8 链接
        val videoUrl =
            "https://vz-e8524359-a55.b-cdn.net/eddf6909-8737-4743-b16a-f52ea5e3ffba/play_720p.mp4"
        val mediaItem = MediaItem.fromUri(videoUrl)
        player.setMediaItem(mediaItem)

        // 准备播放器
        player.prepare()
        player.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release() // 释放资源
        addHistoryRecord() // 调用新增历史记录接口
    }

    private fun addHistoryRecord() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val videoId = intent.getStringExtra("VIDEO_ID")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/") // 使用你的后端URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val historyApi = retrofit.create(HistoryApi::class.java)

        if (videoId != null) {
            val historyData = HistoryAddRequest(
                deviceId = androidId,
                videoId = videoId,
                playDuration = 50
            )

            val call = historyApi.addHistory(historyData)

            call.enqueue(object : Callback<HistoryAddResponse> {
                override fun onResponse(
                    call: Call<HistoryAddResponse>,
                    response: Response<HistoryAddResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.v("VideoPlayActivity", "历史记录新增成功")
                    } else {
                        Log.e("VideoPlayActivity", "新增历史记录失败: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<HistoryAddResponse>, t: Throwable) {
                    Log.e("VideoPlayActivity", "网络错误: ${t.message}")
                }
            })
        } else {
            Log.e("VideoPlayActivity", "视频ID为空")
        }
    }
}
