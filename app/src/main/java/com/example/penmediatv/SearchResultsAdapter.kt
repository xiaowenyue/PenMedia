package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<SearchResultsAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.name
            binding.movieDescription.text = movie.details
            binding.moviePoster.setImageResource(movie.imageResId)
            binding.btnWatch.setOnClickListener {
                val intent = Intent(binding.root.context, MovieDetailsActivity::class.java)
                intent.putExtra("movie", movie)
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
}