package com.example.penmediatv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemCarouselTitleBinding

class CarouselAdapter(private val items: List<Movie>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    // 使用 ViewBinding 来替代手动查找视图
    class CarouselViewHolder(val binding: ItemCarouselTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        // 使用 ViewBinding 进行视图的膨胀
        val binding = ItemCarouselTitleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        // 通过 binding 直接访问视图
        holder.binding.imageView.setImageResource(item.imageResId)
        holder.binding.movieName.text = item.name
        holder.binding.movieDetails.text = item.details
        holder.binding.playTime.text = item.minorDetails
        // 为 playTime 添加焦点事件监听
        holder.binding.playTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 处理获得焦点时的逻辑
                holder.binding.playTime.setBackgroundColor(holder.itemView.context.getColor(R.color.orange))
            } else {
                // 处理失去焦点时的逻辑#f2f6f8
                holder.binding.playTime.setBackgroundColor(holder.itemView.context.getColor(R.color.play))
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
