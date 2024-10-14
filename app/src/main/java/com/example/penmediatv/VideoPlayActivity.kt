package com.example.penmediatv

import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.R

class VideoPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        val videoWebView = findViewById<WebView>(R.id.videoWebView)
        videoWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()//忽略SSL错误
            }
        }
        // 启用JavaScript
        val webSettings: WebSettings = videoWebView.settings
        webSettings.javaScriptEnabled = true

        // 使用WebViewClient确保WebView在当前应用中打开
        videoWebView.webViewClient = WebViewClient()

        // 这里加载你的Bunny视频链接
        val videoUrl = "https://iframe.mediadelivery.net/play/314818/60b32cdf-b065-4a7d-af08-affec3f3ea92"
        videoWebView.loadUrl(videoUrl)
    }
}
