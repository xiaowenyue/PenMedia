package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemMovieBinding

class MovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.name
//            binding.movieImage.setImageResource(movie.imageResId)
            binding.movieItem.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.mcvPic.strokeWidth = 6
                    binding.mcvPic.strokeColor =
                        ContextCompat.getColor(binding.root.context, R.color.white)
                    binding.mcvPic.cardElevation = 15f
                    val scaleUp = ScaleAnimation(
                        1f, 1.1f, 1f, 1.1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                    scaleUp.duration = 300
                    scaleUp.fillAfter = true
                    binding.movieItem.startAnimation(scaleUp)
                } else {
                    binding.mcvPic.strokeWidth = 0
                    binding.mcvPic.cardElevation = 0f
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
