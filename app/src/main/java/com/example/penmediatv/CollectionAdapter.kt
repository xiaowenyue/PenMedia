package com.example.penmediatv

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.Data.CollectionItem
import com.example.penmediatv.databinding.ItemCollectionBinding

class CollectionAdapter(private val collectionList: MutableList<CollectionItem>) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {
    class CollectionViewHolder(private val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: CollectionItem) {
            binding.title.text = movie.videoNameEn  // 你可以根据需要显示 videoNameEn 或 videoNameZh
            // 假设你有一个加载图片的方法
            Glide.with(binding.root)
                .load(movie.videoCover)
                .placeholder(R.drawable.movie) // 设置一个占位符
                .error(R.drawable.movie) // 如果加载失败，显示一个默认图片
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
        holder.bind(collectionList[position])
    }

    override fun getItemCount(): Int {
        return collectionList.size
    }

    fun updateMovies(newMovies: List<CollectionItem>) {
        collectionList.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun clearMovies() {
        collectionList.clear() // 清空当前列表
        notifyDataSetChanged() // 通知RecyclerView数据已清空，刷新视图
    }
}