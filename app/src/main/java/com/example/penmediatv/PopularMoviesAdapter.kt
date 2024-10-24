package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.Data.TrendRecommendItem
import com.example.penmediatv.databinding.ItemSearchMovieBinding

class PopularMoviesAdapter(private val movies: MutableList<TrendRecommendItem>) :
    RecyclerView.Adapter<PopularMoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: TrendRecommendItem) {
            binding.btnMovieName.text = movie.videoNameEn
            binding.btnMovieName.setOnClickListener {
                val intent = Intent(binding.root.context, MovieDetailsActivity::class.java)
                intent.putExtra("VIDEO_ID", movie.videoId)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun addMovies(newMovies: List<TrendRecommendItem>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}
