package com.example.penmediatv

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.ActivityTvDetailsBinding

class TvDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTvDetailsBinding
    private lateinit var movie: Movie
    private var isCollected = false  // 用于记录是否收藏

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movie = intent.getParcelableExtra("MOVIE_DATA") ?: Movie(
            "movieName",
            R.drawable.movie,
            episodes = 50
        )

        binding.btWatchNow.setOnClickListener {
            Toast.makeText(this, "Watch Now", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MoviePlayActivity::class.java)
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
        // 动态生成剧集范围按钮
        generateEpisodeRangeButtons(movie.episodes)
    }

    // 动态生成剧集范围按钮 (1-10, 11-20,...)
    private fun generateEpisodeRangeButtons(totalEpisodes: Int) {
        val rangesContainer = binding.llEpisodeRanges
        rangesContainer.removeAllViews() // 清空已有的范围按钮
        // 每个范围 10 集
        val numRanges = (totalEpisodes + 9) / 10
        for (i in 0 until numRanges) {
            val start = i * 10 + 1
            val end = minOf((i + 1) * 10, totalEpisodes)
            val rangeButton = AppCompatButton(ContextThemeWrapper(this, R.style.EpisodeRangeButton), null, R.style.EpisodeRangeButton).apply {
                text = "$start-$end"
                setOnClickListener {
                    updateEpisodes(start, end)
                }
            }
            rangesContainer.addView(rangeButton)
        }
    }

    // 动态生成具体剧集按钮
    private fun updateEpisodes(start: Int, end: Int) {
        val episodesContainer = binding.llEpisodes
        episodesContainer.removeAllViews() // 清空已有的剧集按钮
        for (i in start..end) {
            // 创建具体剧集按钮
            val episodeButton = AppCompatButton(ContextThemeWrapper(this, R.style.EpisodeButton), null, R.style.EpisodeButton).apply {
                text = i.toString()
                setOnClickListener {
                    Toast.makeText(this@TvDetailsActivity, "点击第 $i 集", Toast.LENGTH_SHORT).show()
                }
            }
            // 将按钮添加到容器中
            episodesContainer.addView(episodeButton)
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