package com.example.penmediatv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.databinding.ItemRelevantRecommendationBinding

class RelevantRecommendationAdapter(private val movies: MutableList<AnimationItem>) :
    RecyclerView.Adapter<RelevantRecommendationAdapter.RelevantViewHolder>() {

    class RelevantViewHolder(private val binding: ItemRelevantRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: AnimationItem) {
            binding.tvMovieRecommendTitle.text = movie.videoNameEn
            binding.region.text = movie.otherInfo.region + " | " + movie.otherInfo.releaseDate
            // 加载封面图片（使用 Glide）
            Glide.with(binding.root.context)
                .load(movie.videoCover)  // 从 API 返回的数据中获取封面图片链接
                .placeholder(R.drawable.movie) // 设置一个占位符
                .error(R.drawable.movie) // 如果加载失败，显示一个默认图片
                .into(binding.ivMovieRecommend)
            binding.itemMovieRecommend.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelevantViewHolder {
        val binding = ItemRelevantRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RelevantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RelevantViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMovies(newMovies: List<AnimationItem>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}