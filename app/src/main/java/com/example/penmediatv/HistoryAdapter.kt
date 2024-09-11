package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemHistoryBinding

class HistoryAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.title.text = movie.name
            binding.pic.setImageResource(movie.imageResId)
            binding.item.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    Toast.makeText(
                        binding.root.context,
                        "Focused on ${movie.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    println("no focus")
                }
            }
            binding.item.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, TvDetailsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}