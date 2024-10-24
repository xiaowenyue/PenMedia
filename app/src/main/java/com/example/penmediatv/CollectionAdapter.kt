package com.example.penmediatv

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.API.EpisodeApi
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.Data.CollectionItem
import com.example.penmediatv.Data.EpisodeResponse
import com.example.penmediatv.databinding.ItemCollectionBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CollectionAdapter(private val collectionList: MutableList<CollectionItem>) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    // 将 CollectionAdapter 传递给 ViewHolder
    inner class CollectionViewHolder(private val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: CollectionItem) {
            binding.title.text = movie.videoNameEn
            Glide.with(binding.root)
                .load(movie.videoCover)
                .into(binding.pic)

            // 使用外部类的 itemClick 方法
            binding.item.setOnClickListener {
                itemClick(movie.videoId, binding.root.context)
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
        collectionList.clear()
        notifyDataSetChanged()
    }

    // 外部类的 itemClick 方法
    private fun itemClick(videoId: String, context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val episodeApi = retrofit.create(EpisodeApi::class.java)
        val call = episodeApi.getEpisode(videoId, 1, 10)
        call.enqueue(object : retrofit2.Callback<EpisodeResponse> {
            override fun onResponse(
                call: retrofit2.Call<EpisodeResponse>,
                response: retrofit2.Response<EpisodeResponse>
            ) {
                if (response.isSuccessful) {
                    val episodeResponse = response.body()?.data
                    if (episodeResponse != null) {
                        val intent = if (episodeResponse.records.size <= 1) {
                            Intent(context, MovieDetailsActivity::class.java)
                        } else {
                            Intent(context, TvDetailsActivity::class.java).apply {
                                putExtra("VIDEO_EPISODE", episodeResponse.records.size)
                            }
                        }
                        intent.putExtra("VIDEO_ID", videoId)
                        context.startActivity(intent)
                    }
                } else {
                    ErrorHandler.handleUnsuccessfulResponse(context, this::class.java.simpleName)
                }
            }

            override fun onFailure(call: retrofit2.Call<EpisodeResponse>, t: Throwable) {
                ErrorHandler.handleFailure(t, context, this::class.java.simpleName)
            }
        })
    }
}