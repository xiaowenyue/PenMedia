package com.example.penmediatv

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivityMoviePlayBinding
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayConfig
import com.tencent.rtmp.TXVodPlayer

class MoviePlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviePlayBinding
    private lateinit var mVodPlayer: TXVodPlayer
    private var isAdPlaying = false
    private var mainVideoUrl = "http://vjs.zencdn.net/v/oceans.mp4"
    private var adVideoUrl =
        "https://cdn.coverr.co/videos/coverr-serene-surfer-gazing-at-the-sea/720p.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //创建 player 对象
        mVodPlayer = TXVodPlayer(this)

        // 创建配置对象
        val playConfig = TXVodPlayConfig()
        mVodPlayer.setConfig(playConfig)
        mVodPlayer.setPlayerView(binding.videoView)

        // 设置监听器
        mVodPlayer.setVodListener(object : ITXVodPlayListener {
            override fun onPlayEvent(player: TXVodPlayer?, event: Int, param: Bundle?) {
                when (event) {
                    TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                        binding.loadingProgressBar.visibility = View.GONE
                        binding.videoView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNetStatus(player: TXVodPlayer?, status: Bundle?) {
            }
        })

        binding.clView.requestFocus()
        mVodPlayer.startVodPlay(mainVideoUrl)
        binding.clView.setOnClickListener{
            if (mVodPlayer.isPlaying) {
                mVodPlayer.pause()
                binding.pauseOverlay.visibility = View.VISIBLE
            } else {
                mVodPlayer.resume()
                binding.pauseOverlay.visibility = View.GONE
            }
        }
    }

    private fun playAd() {
        mVodPlayer.pause()
        isAdPlaying = true
        mVodPlayer.startVodPlay(adVideoUrl)

        mVodPlayer.setVodListener(object : ITXVodPlayListener {
            override fun onPlayEvent(player: TXVodPlayer?, event: Int, param: Bundle?) {
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END && isAdPlaying) {
                    isAdPlaying = false
                    mVodPlayer.startVodPlay(mainVideoUrl)
                }
            }

            override fun onNetStatus(player: TXVodPlayer?, status: Bundle?) {
            }
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        mVodPlayer.stopPlay(true)
        binding.videoView.onDestroy()
    }
}