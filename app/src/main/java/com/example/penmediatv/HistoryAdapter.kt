package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.HistoryItem
import com.example.penmediatv.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyList: MutableList<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: HistoryItem) {
            binding.title.text = movie.videoNameEn
            Glide.with(binding.root)
                .load(movie.videoCover)
//                .placeholder(R.drawable.movie) // 设置一个占位符
//                .error(R.drawable.movie) // 如果加载失败，显示一个默认图片
                .into(binding.pic)
            binding.item.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.mcvPic.strokeWidth = 6
                    binding.mcvPic.strokeColor =
                        ContextCompat.getColor(binding.root.context, R.color.white)
                    val scaleUp = ScaleAnimation(
                        1f, 1.1f, 1f, 1.1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    scaleUp.duration = 300
                    scaleUp.fillAfter = true
                    binding.item.startAnimation(scaleUp)
                } else {
                    binding.mcvPic.strokeWidth = 0
                    val scaleDown = ScaleAnimation(
                        1.1f, 1f, 1.1f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    scaleDown.duration = 300
                    scaleDown.fillAfter = true
                    binding.item.startAnimation(scaleDown)
                }
            }
            binding.item.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, VideoPlayActivity::class.java)
                intent.putExtra("VIDEO_ID", movie.videoId)
                intent.putExtra("WATCH_DURATION", movie.playedDuration.toLong())
                intent.putExtra("VIDEO_URL", movie.videoUrl)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun updateHistories(newHistories: List<HistoryItem>) {
        historyList.addAll(newHistories)
        notifyDataSetChanged()
    }

    fun clearMovies() {
        historyList.clear() // 清空当前列表
        notifyDataSetChanged() // 通知RecyclerView数据已清空，刷新视图
    }
}