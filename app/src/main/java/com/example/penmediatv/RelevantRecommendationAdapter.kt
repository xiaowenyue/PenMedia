package com.example.penmediatv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemRelevantRecommendationBinding

class RelevantRecommendationAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<RelevantRecommendationAdapter.RelevantViewHolder>() {

    class RelevantViewHolder(private val binding: ItemRelevantRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
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
}