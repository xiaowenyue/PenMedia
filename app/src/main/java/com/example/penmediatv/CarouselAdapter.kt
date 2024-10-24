package com.example.penmediatv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.Data.SwiperItem
import com.example.penmediatv.databinding.ItemCarouselTitleBinding

class CarouselAdapter(val items: List<SwiperItem>) :
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
        Glide.with(holder.itemView)
            .load(item.videoCover)
            .into(holder.binding.imageView)
        holder.binding.movieName.text = item.videoNameEn
        holder.binding.movieDetails.text = item.videoDesc
        holder.binding.playTime.text = item.subTitle
    }

    override fun getItemCount(): Int = items.size
}
