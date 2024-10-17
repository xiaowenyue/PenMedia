package com.example.penmediatv

import android.net.http.SslError
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.API.HistoryApi
import com.example.penmediatv.Data.HistoryAddRequest
import com.example.penmediatv.Data.HistoryAddResponse
import com.example.penmediatv.Data.HistoryData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoPlayActivity : AppCompatActivity() {
    private lateinit var videoWebView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        WebView.setWebContentsDebuggingEnabled(true)

        videoWebView = findViewById(R.id.videoWebView)
        videoWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed() // 忽略SSL错误
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.v("VideoPlayActivity", "页面加载完成")

                // 等待页面完全加载后再调用 togglePlayPause
                videoWebView.evaluateJavascript("typeof togglePlayPause === 'function';") { result ->
                    if (result == "true") {
                        Log.v("VideoPlayActivity", "togglePlayPause 函数可用")
                    } else {
                        Log.v("VideoPlayActivity", "未找到 togglePlayPause 函数")
                    }
                }
            }
        }
        // 启用跨域访问和JavaScript
        val webSettings: WebSettings = videoWebView.settings
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.javaScriptEnabled = true

        // 使用自定义的HTML字符串
        val htmlData = """
            <html>
            <body>
                <!-- Bunny视频iframe -->
                <iframe id="iframe" src="https://iframe.mediadelivery.net/embed/314818/60b32cdf-b065-4a7d-af08-affec3f3ea92"
                        width="100%" height="100%" frameborder="0" allowfullscreen></iframe>

                <!-- 加载player.js脚本 -->
                <script type="text/javascript" src="https://assets.mediadelivery.net/playerjs/player-0.1.0.min.js"></script>
                <script type="text/javascript">
                    var player;
                    document.addEventListener('DOMContentLoaded', function() {
                        player = new playerjs.Player('iframe');
                        console.log('Player.js脚本加载成功');
                        console.log(player);
                
                        player.on('ready', function() {
                            console.log('Player is ready');
                            player.play();
                            console.log("获取播放器状态"+player.play);
                            window.isPaused = false;  // 播放视频
                            player.on('play', function() {
                                console.log("播放事件触发");
                            });

                            // 监听键盘Enter事件
                            document.addEventListener('keydown', function(event) {
                                console.log('按键事件捕获: ' + event.code);
                                if (event.code === 'Enter' || event.keyCode === 13) {
                                    console.log('Enter键被按下');
                                    togglePlayPause();
                                }
                            });
                        });
                    });
                
                    function togglePlayPause() {
                        console.log('togglePlayPause函数被调用');
                        console.log('视频播放状态:'+window.isPaused);
                        if (window.isPaused) {
                            console.log('播放视频');
                            player.getState(function(state) {
                                console.log("当前播放器状态: " + state);
                                if (state !== "playing") {
                                    console.log("当前播放器未在播放，调用播放方法");
                                    player.play();  // 确保播放器未在播放时才调用
                                }
                            });
                            window.isPaused = false;
                            return "playing";
                        } else {
                            console.log('暂停视频');
                            player.pause();
                            window.isPaused = true;
                            return "paused";
                        }
                    }
                    // Enter键按下的功能
                    function simulateEnterKey() {
                        const event = new KeyboardEvent('keydown', {
                            code: 'Enter',
                            keyCode: 13,
                            which: 13,
                            key: 'Enter'
                        });
                        document.dispatchEvent(event);
                    }
                </script>
                <!-- 测试按钮 -->
                <button onclick="simulateEnterKey()">Enter键</button>
            </body>
            </html>
        """.trimIndent()

        // 加载本地HTML字符串
        videoWebView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
        videoWebView.requestFocus() // 确保 WebView 具有焦点
    }

    // 监听遥控器的OK键
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            Log.v("VideoPlayActivity", "OK键被按下")
            videoWebView.evaluateJavascript("togglePlayPause();") { result ->
                Log.v("VideoPlayActivity", "JavaScript执行结果: $result")
                if (result == "\"playing\"") {
                    Log.v("VideoPlayActivity", "视频正在播放")
                } else if (result == "\"paused\"") {
                    Log.v("VideoPlayActivity", "视频已暂停")
                }
            }
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
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
