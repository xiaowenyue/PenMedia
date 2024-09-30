package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ScrollView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemMovieBinding

class MovieAdapter(private val movies: List<Movie>, private val scrollView: ScrollView) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, scrollView: ScrollView) {
            binding.movieTitle.text = movie.name
//            binding.movieImage.setImageResource(movie.imageResId)
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
            binding.movieItem.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, MovieDetailsActivity::class.java)
                context.startActivity(intent)
            }
        }

        private fun scrollToCenter(view: View, scrollView: ScrollView) {
            val location = IntArray(2)
            view.getLocationOnScreen(location)

            val scrollViewLocation = IntArray(2)
            scrollView.getLocationOnScreen(scrollViewLocation)

            // 获取 item 的 Y 坐标，并计算它应该滚动的位置
            val itemY = location[1]
            val scrollViewY = scrollViewLocation[1]

            // 计算 ScrollView 应该滚动的距离
            val scrollAmount = itemY - scrollViewY - scrollView.height / 2 + view.height / 2

            // 执行平滑滚动
            scrollView.smoothScrollBy(0, scrollAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], scrollView)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
