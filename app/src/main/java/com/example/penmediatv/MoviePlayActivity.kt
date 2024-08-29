package com.example.penmediatv

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivityMoviePlayBinding
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXPlayInfoParams
import com.tencent.rtmp.TXVodPlayConfig
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

        // 创建配置对象
        val playConfig = TXVodPlayConfig()
        // 设置配置
        mVodPlayer.setConfig(playConfig)

        // 设置监听器
        mVodPlayer.setVodListener(object : ITXVodPlayListener {
            override fun onPlayEvent(player: TXVodPlayer?, event: Int, param: Bundle?) {
                when (event) {
                    TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                        // 隐藏海报和加载框，显示视频
                        binding.loadingProgressBar.visibility = View.GONE
                        binding.videoView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNetStatus(player: TXVodPlayer?, s9tatus: Bundle?) {
                // 处理网络状态
            }
        })

        //关联 player 对象与视频渲染 view
        mVodPlayer.setPlayerView(binding.videoView)

        //播放 URL 视频资源
//        val url = "https://cdn.coverr.co/videos/coverr-serene-surfer-gazing-at-the-sea/720p.mp4"
        val url = "http://vjs.zencdn.net/v/oceans.mp4"
        mVodPlayer.startVodPlay(url)


        // 推荐使用下面的新接口
// psign 即播放器签名，签名介绍和生成方式参见链接：https://cloud.tencent.com/document/product/266/42436
//        val playInfoParam = TXPlayInfoParams(
//            1329031633,  // 腾讯云账户的appId
//            "1397757891767785731",  // 视频的fileId
//            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBJZCI6MTMyOTAzMTYzMywiZmlsZUlkIjoiMTM5Nzc1Nzg5MTc2Nzc4NTczMSIsImN1cnJlbnRUaW1lU3RhbXAiOjE3MjQ5MDIzOTIsImNvbnRlbnRJbmZvIjp7ImF1ZGlvVmlkZW9UeXBlIjoiT3JpZ2luYWwifSwidXJsQWNjZXNzSW5mbyI6eyJkb21haW4iOiIxMzI5MDMxNjMzLnZvZC1xY2xvdWQuY29tIiwic2NoZW1lIjoiSFRUUFMifX0.DvysgSk3OTKyZSZE8grg8ywnQObl0FHrGtRbtvvlVTk"
//        ) // 播放器签名
//        mVodPlayer.startVodPlay(playInfoParam)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mVodPlayer.stopPlay(true) // true 代表清除最后一帧画面
        binding.videoView.onDestroy()
    }
}