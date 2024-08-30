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

class MoviePlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviePlayBinding
    private lateinit var mVodPlayer: TXVodPlayer
    private var isAdPlaying = false
    private var mainVideoUrl = "http://vjs.zencdn.net/v/oceans.mp4"
    private var adVideoUrl = "https://cdn.coverr.co/videos/coverr-serene-surfer-gazing-at-the-sea/720p.mp4"

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

            override fun onNetStatus(player: TXVodPlayer?, status: Bundle?) {}
        })

        binding.clView.requestFocus()
        mVodPlayer.startVodPlay(mainVideoUrl)
        playAd()

        // Click listener to toggle pause/play
        binding.clView.setOnClickListener {
            if (mVodPlayer.isPlaying) {
                pauseVideo()
            } else {
                resumeVideo()
            }
        }

        // Set up playback controls
        setupPlaybackControls()
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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mVodPlayer.setRate(speedOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun pauseVideo() {
        mVodPlayer.pause()
        binding.pauseOverlay.visibility = View.VISIBLE
        binding.bottomControls.visibility = View.VISIBLE
    }

    private fun resumeVideo() {
        mVodPlayer.resume()
        binding.pauseOverlay.visibility = View.GONE
        binding.bottomControls.visibility = View.GONE
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

            override fun onNetStatus(player: TXVodPlayer?, status: Bundle?) {}
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        mVodPlayer.stopPlay(true)
        binding.videoView.onDestroy()
    }
}
