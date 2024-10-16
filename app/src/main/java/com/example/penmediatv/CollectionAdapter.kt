package com.example.penmediatv

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.Data.CollectionItem
import com.example.penmediatv.databinding.ItemCollectionBinding

class CollectionAdapter(private val movies: MutableList<CollectionItem>) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {
    class CollectionViewHolder(private val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: CollectionItem) {
            binding.title.text = movie.videoNameEn  // 你可以根据需要显示 videoNameEn 或 videoNameZh
            // 假设你有一个加载图片的方法
            Glide.with(binding.root)
                .load(movie.videoCover)
                .into(binding.pic)
            binding.item.setOnClickListener {}
            binding.item.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    // 处理获取焦点时的逻辑
                } else {
                    // 处理失去焦点时的逻辑
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMovies(newMovies: List<CollectionItem>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}