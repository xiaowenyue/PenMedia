package com.example.penmediatv

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivityMoviePlayBinding
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayConfig
import com.tencent.rtmp.TXVodPlayer
import kotlinx.coroutines.*

class MoviePlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviePlayBinding
    private lateinit var mVodPlayer: TXVodPlayer
    private var isAdPlaying = false
    private var mainVideoUrl = "http://vjs.zencdn.net/v/oceans.mp4"
    private var adVideoUrl =
        "https://cdn.coverr.co/videos/coverr-serene-surfer-gazing-at-the-sea/720p.mp4"

    // 用于更新进度的协程
    private var progressJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 创建 player 对象
        mVodPlayer = TXVodPlayer(this)

        // 创建配置对象
        val playConfig = TXVodPlayConfig()
        mVodPlayer.setConfig(playConfig)
        mVodPlayer.setPlayerView(binding.videoView)

        // 设置统一的监听器
        mVodPlayer.setVodListener(object : ITXVodPlayListener {
            override fun onPlayEvent(player: TXVodPlayer?, event: Int, param: Bundle?) {
                when (event) {
                    TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        binding.videoView.visibility = View.VISIBLE
                        // 启动进度更新
                        startProgressUpdate()
                    }
                    TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED -> {
                        // 播放准备就绪
                        val duration = mVodPlayer.duration.toInt() * 1000
                        if (duration > 0) {
                            // 可以在这里启动进度更新
                            startProgressUpdate()
                        }
                    }
                    TXLiveConstants.PLAY_EVT_PLAY_END -> {
                        if (isAdPlaying) {
                            isAdPlaying = false
                            mVodPlayer.startVodPlay(mainVideoUrl)
                        } else {
                            // 主视频播放结束，可以进行相应处理
                        }
                    }
                    // 处理其他事件
                }
            }

            override fun onNetStatus(player: TXVodPlayer?, status: Bundle?) {}
        })

        binding.clView.requestFocus()
        mVodPlayer.startVodPlay(mainVideoUrl)
        playAd()
        setupPlaybackControls()

        // 点击事件监听
        binding.clView.setOnClickListener {
            if (mVodPlayer.isPlaying) {
                pauseVideo()
            } else {
                resumeVideo()
            }
        }
    }

    // 启动定时任务来更新进度
    private fun startProgressUpdate() {
        // 如果已经有任务在运行，先取消
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive && mVodPlayer.isPlaying) {
                val currentPosition = mVodPlayer.currentPlaybackTime.toInt() * 1000 // 转换为毫秒
                val duration = mVodPlayer.duration.toInt() * 1000 // 转换为毫秒
                if (duration > 0) {
                    updateProgress(currentPosition, duration)
                }
                delay(1000) // 每秒更新一次
            }
        }
    }

    // 更新播放进度和时长
    fun updateProgress(currentPosition: Int, duration: Int) {
        // 防止除以零
        if (duration == 0) {
            return
        }

        val progress = (currentPosition * 100 / duration)
        binding.progressBar.progress = progress.coerceIn(0, 100) // 确保进度在0-100之间

        // 转换时间格式为 "mm:ss | mm:ss"
        val currentMinutes = (currentPosition / 1000) / 60
        val currentSeconds = (currentPosition / 1000) % 60
        val totalMinutes = (duration / 1000) / 60
        val totalSeconds = (duration / 1000) % 60

        binding.tvTime.text = String.format(
            "%02d:%02d | %02d:%02d",
            currentMinutes,
            currentSeconds,
            totalMinutes,
            totalSeconds
        )
    }

    private fun setupPlaybackControls() {
        // Play/Pause button
        binding.playPauseButton.setOnClickListener {
            if (mVodPlayer.isPlaying) {
                pauseVideo()
            } else {
                resumeVideo()
            }
        }

        // Previous button
        binding.prevButton.setOnClickListener {
            // Implement the logic to play the previous video or seek back
        }

        // Next button
        binding.nextButton.setOnClickListener {
            // Implement the logic to play the next video or seek forward
        }

        // Speed spinner
        val speedOptions = arrayOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, speedOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.speedSpinner.adapter = adapter
        binding.speedSpinner.setSelection(2) // Default to 1.0x speed
        binding.speedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mVodPlayer.setRate(speedOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun pauseVideo() {
        mVodPlayer.pause()
        binding.pauseOverlay.visibility = View.VISIBLE
        binding.bottomControls.visibility = View.VISIBLE
        // 取消进度更新任务
        progressJob?.cancel()
    }

    private fun resumeVideo() {
        mVodPlayer.resume()
        binding.pauseOverlay.visibility = View.GONE
        binding.bottomControls.visibility = View.GONE
        // 重新启动进度更新任务
        startProgressUpdate()
    }

    private fun playAd() {
        mVodPlayer.pause()
        isAdPlaying = true
        mVodPlayer.startVodPlay(adVideoUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressJob?.cancel()
        mVodPlayer.stopPlay(true)
        binding.videoView.onDestroy()
    }
}
