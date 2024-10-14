package com.example.penmediatv

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.ActivityMovieDetailsBinding

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private var isCollected = false  // 用于记录是否收藏

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btWatchNow.setOnClickListener {
            Toast.makeText(this, "Watch Now", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VideoPlayActivity::class.java)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = RelevantRecommendationAdapter(getMovies())
        binding.btnCollect.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_collected)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            // 根据是否已经收藏，设置弹窗内容
            val imageView = dialog.findViewById<ImageView>(R.id.iv_connected)
            val titleTextView = dialog.findViewById<TextView>(R.id.tv_title)
            val subTextView = dialog.findViewById<TextView>(R.id.tv_content)
            if (!isCollected) {
                // 未收藏时，点击收藏
                ContextCompat.getDrawable(this, R.drawable.ic_connect)?.let {
                    binding.btnCollect.setCompoundDrawablesWithIntrinsicBounds(
                        it,
                        null,
                        null,
                        null
                    )
                }
                imageView.setImageResource(R.drawable.ic_connected)  // 设定图片
                titleTextView.text = "Collected"
                subTextView.text = "View at the personal center"
                isCollected = true  // 更新收藏状态为已收藏
            } else {
                // 已收藏时，点击修改弹窗内容
                ContextCompat.getDrawable(this, R.drawable.ic_heart)?.let {
                    binding.btnCollect.setCompoundDrawablesWithIntrinsicBounds(
                        it,
                        null,
                        null,
                        null
                    )
                }
                imageView.setImageResource(R.drawable.ic_unconnected)  // 替换图片
                titleTextView.text = "Uncollected"
                subTextView.visibility = TextView.GONE
            }
            dialog.show()
            // 使用 Handler 使弹窗在3秒后自动消失
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)
        }
    }

    private fun getMovies(): List<Movie> {
        // Generate dummy movie data
        return listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.ic_search),
            Movie("Movie 3", R.drawable.ic_history),
            Movie("Movie 4", R.drawable.ic_mine),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
            Movie("Movie 7", R.drawable.ic_mine),
            Movie("Movie 8", R.drawable.ic_search)
        )
    }
}