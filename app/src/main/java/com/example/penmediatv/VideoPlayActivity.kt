package com.example.penmediatv

import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

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
                <iframe id="bunnyIframe" src="https://iframe.mediadelivery.net/embed/314818/60b32cdf-b065-4a7d-af08-affec3f3ea92"
                        width="100%" height="100%" frameborder="0" allowfullscreen></iframe>

                <!-- 加载player.js脚本 -->
                <script type="text/javascript" src="https://assets.mediadelivery.net/playerjs/player-0.1.0.min.js"></script>
                <script type="text/javascript">
                    var player;
                    document.addEventListener('DOMContentLoaded', function() {
                        player = new playerjs.Player('bunnyIframe');
                        console.log('Player.js脚本加载成功');
                        
                        player.on('ready', function() {
                            console.log('Player is ready');
                            window.isPaused = false;  // 初始化状态

                            window.addEventListener('keydown', function(event) {
                                console.log('按键事件捕获: ' + event.code);
                                if (event.code === 'Enter') {
                                    console.log('Enter键被按下，切换播放/暂停');
                                    togglePlayPause();
                                }
                            });
                        });
                    });

                    function togglePlayPause() {
                        if (window.isPaused) {
                            player.play();
                            window.isPaused = false;
                        } else {
                            player.pause();
                            window.isPaused = true;
                        }
                    }
                </script>
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
                if (result == "null") {
                    Log.v("VideoPlayActivity", "JavaScript没有返回有效结果")
                }
            }
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}
