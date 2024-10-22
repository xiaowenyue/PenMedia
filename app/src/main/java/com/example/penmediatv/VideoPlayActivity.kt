package com.example.penmediatv

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
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

@androidx.media3.common.util.UnstableApi
class VideoPlayActivity : AppCompatActivity() {
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private var watchDuration: Long = 0 // 记录观看时长

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        playerView = findViewById(R.id.player_view)

        // 配置超时时间
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(15000) // 设置连接超时为15秒
            .setReadTimeoutMs(15000)    // 设置读取数据超时为15秒
            .setTransferListener(null)

        // 初始化 ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // 设置播放源
        val m3u8Url =
            "https://vz-e8524359-a55.b-cdn.net/a1032a95-e4dc-41ff-91cf-46c6465cf9fa/playlist.m3u8"
        val mediaItem = MediaItem.fromUri(Uri.parse(m3u8Url))
        exoPlayer.setMediaItem(mediaItem)

        // 添加错误监听器
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                if (error.cause is HttpDataSource.HttpDataSourceException) {
                    val dialog = Dialog(this@VideoPlayActivity)
                    dialog.setContentView(R.layout.dialog_network_dismiss)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 2000)
                }
            }
        })

        // 准备播放
        exoPlayer.prepare()
        exoPlayer.play()

        // 如果有历史记录进度，设置到上次观看的位置
        val historyDuration = intent.getLongExtra("WATCH_DURATION", 0L)
        if (historyDuration > 0) {
            exoPlayer.seekTo(historyDuration)
        } else {
            exoPlayer.seekTo(0)
        }
        exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        watchDuration = exoPlayer.currentPosition // 获取当前的播放位置（观看时长）
        exoPlayer.release() // 释放资源
        addHistoryRecord(watchDuration) // 将观看时长传递给后端
    }

    // 新增历史记录
    private fun addHistoryRecord(playDuration: Long) {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val videoId = intent.getStringExtra("VIDEO_ID")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/") // 使用你的后端URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val historyApi = retrofit.create(HistoryApi::class.java)

        if (videoId != null) {
            Log.v("VideoPlayActivity", "Android ID：$androidId")
            Log.v("VideoPlayActivity", "视频ID：$videoId")
            Log.v("VideoPlayActivity", "观看时长：$playDuration")
            val historyData = HistoryAddRequest(
                deviceId = androidId,
                videoId = videoId,
                playDuration = playDuration.toInt()
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
