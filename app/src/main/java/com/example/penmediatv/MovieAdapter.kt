package com.example.penmediatv

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.databinding.ItemMovieBinding

// 修改为 AnimationItem, 使用你从API获取的模型类
class MovieAdapter(private val movies: List<AnimationItem>, private val scrollView: ScrollView) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 绑定 API 返回的数据到UI
        fun bind(movie: AnimationItem, scrollView: ScrollView) {
            // 设置电影名称和描述
            binding.movieTitle.text = movie.videoNameZh

            // 加载封面图片（使用 Glide）
            Glide.with(binding.root.context)
                .load(movie.videoCover)  // 从 API 返回的数据中获取封面图片链接
                .placeholder(R.drawable.movie) // 设置一个占位符
                .error(R.drawable.movie) // 如果加载失败，显示一个默认图片
                .into(binding.movieImage)

            // 设置获取焦点时的动画效果
            binding.movieItem.setOnFocusChangeListener { _, hasFocus ->
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
                    binding.movieItem.startAnimation(scaleUp)
                    scrollToCenter(binding.movieItem, scrollView)
                } else {
                    binding.mcvPic.strokeWidth = 0
                    val scaleDown = ScaleAnimation(
                        1.1f, 1f, 1.1f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    scaleDown.duration = 300
                    scaleDown.fillAfter = true
                    binding.movieItem.startAnimation(scaleDown)
                }
            }

            // 设置点击事件，跳转到详情页
            binding.movieItem.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, MovieDetailsActivity::class.java)
                // 传递 videoId 作为电影详情的标识符
                intent.putExtra("VIDEO_ID", movie.videoId)
                context.startActivity(intent)
            }
        }

        // 将焦点滚动到中间
        private fun scrollToCenter(view: View, scrollView: ScrollView) {
            val location = IntArray(2)
            view.getLocationOnScreen(location)

            val scrollViewLocation = IntArray(2)
            scrollView.getLocationOnScreen(scrollViewLocation)

            val itemY = location[1]
            val scrollViewY = scrollViewLocation[1]

            val scrollAmount = itemY - scrollViewY - scrollView.height / 2 + view.height / 2
            scrollView.smoothScrollBy(0, scrollAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Log.d("MovieAdapter", "Binding item at position: $position")
        holder.bind(movies[position], scrollView)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
