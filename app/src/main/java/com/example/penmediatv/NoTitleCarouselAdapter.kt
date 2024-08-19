package com.example.penmediatv

import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class NoTitleCarouselAdapter(private val items: List<Movie>) :
    RecyclerView.Adapter<NoTitleCarouselAdapter.NoTitleCarouselViewHolder>() {

    class NoTitleCarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoTitleCarouselViewHolder {
        val view = from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return NoTitleCarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoTitleCarouselViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = items.size
}