package com.example.penmediatv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivityMoviePlayBinding
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.rtmp.TXVodPlayer


class MoviePlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviePlayBinding
    private lateinit var mVodPlayer: TXVodPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //创建 player 对象
        mVodPlayer = TXVodPlayer(this)

        //关联 player 对象与视频渲染 view
        mVodPlayer.setPlayerView(binding.videoView)

        //播放 URL 视频资源
        val url = "http://vjs.zencdn.net/v/oceans.mp4"
        mVodPlayer.startVodPlay(url)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mVodPlayer.stopPlay(true) // true 代表清除最后一帧画面
        binding.videoView.onDestroy()
    }
}