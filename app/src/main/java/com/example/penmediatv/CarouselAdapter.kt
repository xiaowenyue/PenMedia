package com.example.penmediatv

import android.view.LayoutInflater
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarouselAdapter(private val items: List<Movie>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val movieName: TextView = view.findViewById(R.id.movieName)
        val movieDetails: TextView = view.findViewById(R.id.movieDetails)
        val playTime: TextView = view.findViewById(R.id.playTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.movieName.text = item.name
        holder.movieDetails.text = item.details
        holder.playTime.text = item.minorDetails
    }

    override fun getItemCount(): Int = items.size
}

//data class Movie(val imageResId: Int, val movieName: String, val movieDetails: String, val playTime: String)
