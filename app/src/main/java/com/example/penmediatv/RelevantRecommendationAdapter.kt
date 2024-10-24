package com.example.penmediatv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.API.EpisodeApi
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.Data.EpisodeResponse
import com.example.penmediatv.databinding.ItemRelevantRecommendationBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RelevantRecommendationAdapter(private val movies: MutableList<AnimationItem>) :
    RecyclerView.Adapter<RelevantRecommendationAdapter.RelevantViewHolder>() {

    class RelevantViewHolder(private val binding: ItemRelevantRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: AnimationItem) {
            binding.tvMovieRecommendTitle.text = movie.videoNameEn
            binding.region.text = movie.otherInfo.region + " | " + movie.otherInfo.releaseDate
            // 加载封面图片（使用 Glide）
            Glide.with(binding.root.context)
                .load(movie.videoCover)  // 从 API 返回的数据中获取封面图片链接
                .into(binding.ivMovieRecommend)
            binding.itemMovieRecommend.setOnClickListener {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://44.208.55.69/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val episodeApi = retrofit.create(EpisodeApi::class.java)
                val call = episodeApi.getEpisode(movie.videoId, 1, 10)
                call.enqueue(object : retrofit2.Callback<EpisodeResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<EpisodeResponse>,
                        response: retrofit2.Response<EpisodeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val episodeResponse = response.body()?.data
                            if (episodeResponse != null) {
                                val intent = if (episodeResponse.records.size <= 1) {
                                    Intent(binding.root.context, MovieDetailsActivity::class.java)
                                } else {
                                    Intent(binding.root.context, TvDetailsActivity::class.java).apply {
                                        putExtra("VIDEO_EPISODE", episodeResponse.records.size)
                                    }
                                }
                                intent.putExtra("VIDEO_ID", movie.videoId)
                                binding.root.context.startActivity(intent)
                            }
                        } else {
                            ErrorHandler.handleUnsuccessfulResponse(binding.root.context, this::class.java.simpleName)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<EpisodeResponse>, t: Throwable) {
                        ErrorHandler.handleFailure(t, binding.root.context, this::class.java.simpleName)
                    }
                })
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

    fun updateMovies(newMovies: List<AnimationItem>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}