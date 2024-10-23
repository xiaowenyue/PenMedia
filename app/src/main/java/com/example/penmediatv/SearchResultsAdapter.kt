package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.TrendRecommendItem
import com.example.penmediatv.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private val movies: MutableList<TrendRecommendItem>) :
    RecyclerView.Adapter<SearchResultsAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: TrendRecommendItem) {
            binding.movieTitle.text = movie.videoNameEn
            binding.movieDescription.text = movie.videoDesc
            Glide.with(binding.root.context)
                .load(movie.videoCover)  // 从 API 返回的数据中获取封面图片链接
                .placeholder(R.drawable.movie) // 设置一个占位符
                .error(R.drawable.movie) // 如果加载失败，显示一个默认图片
                .into(binding.moviePoster)
            binding.btnWatch.setOnClickListener {
                val intent = Intent(binding.root.context, MovieDetailsActivity::class.java)
                intent.putExtra("VIDEO_ID", movie.videoId)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
    fun updateMovies(newMovies: List<TrendRecommendItem>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}