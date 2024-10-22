package com.example.penmediatv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.SwiperResponse
import com.example.penmediatv.databinding.FragmentTvserivesBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TvSeriesFragment : Fragment() {
    private var _binding: FragmentTvserivesBinding? = null
    private val binding get() = _binding!!
    private var currentPage = 1
    private var pageSize = 10
    private var totalPages = 1 // 从服务器获取的总页数
    private var isLoading = false
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvserivesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchTvSeries(currentPage, pageSize)
        //轮播图
        fetchSwiperTv()

        // 5秒后自动切换焦点到第二个卡片
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.card2.requestFocus()
//        }, 5000)
    }

    private fun fetchSwiperTv() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animationApi = retrofit.create(AnimationApi::class.java)
        val call = animationApi.getSwiperTv()

        call.enqueue(object : Callback<SwiperResponse> {
            override fun onResponse(
                call: Call<SwiperResponse>,
                response: Response<SwiperResponse>
            ) {
                if (response.isSuccessful) {
                    val swiperDate = response.body()?.data
                    if (swiperDate != null && swiperDate.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(swiperDate[0].videoCover)
                            .into(binding.bgTvSeries)
                        Glide.with(requireContext())
                            .load(swiperDate[0].videoCover)
                            .into(binding.ivCard0)
                        Glide.with(requireContext())
                            .load(swiperDate[1].videoCover)
                            .into(binding.ivCard1)
                        // 设置焦点变化监听器来切换图片
                        binding.card1.setOnFocusChangeListener { _, hasFocus ->
                            if (hasFocus) {
                                Glide.with(requireContext())
                                    .load(swiperDate[0].videoCover)
                                    .into(binding.bgTvSeries)
                                binding.card1.strokeWidth = 6
                                binding.card1.strokeColor =
                                    ContextCompat.getColor(requireContext(), R.color.white)
                                val scaleUp = ScaleAnimation(
                                    1f, 1.1f, 1f, 1.1f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleUp.duration = 300
                                scaleUp.fillAfter = true
                                binding.card1.startAnimation(scaleUp)
                            } else {
                                binding.card1.strokeWidth = 0
                                val scaleDown = ScaleAnimation(
                                    1.1f, 1f, 1.1f, 1f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleDown.duration = 300
                                scaleDown.fillAfter = true
                                binding.card1.startAnimation(scaleDown)
                            }
                        }

                        binding.card2.setOnFocusChangeListener { _, hasFocus ->
                            if (hasFocus) {
                                Glide.with(requireContext())
                                    .load(swiperDate[1].videoCover)
                                    .into(binding.bgTvSeries)
                                binding.card2.strokeWidth = 6
                                binding.card2.strokeColor =
                                    ContextCompat.getColor(requireContext(), R.color.white)
                                val scaleUp = ScaleAnimation(
                                    1f, 1.1f, 1f, 1.1f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.4f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleUp.duration = 300
                                scaleUp.fillAfter = true
                                binding.card2.startAnimation(scaleUp)
                            } else {
                                binding.card2.strokeWidth = 0
                                val scaleDown = ScaleAnimation(
                                    1.1f, 1f, 1.1f, 1f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.4f,
                                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleDown.duration = 300
                                scaleDown.fillAfter = true
                                binding.card2.startAnimation(scaleDown)
                            }
                        }
                    } else {
                        Log.e("AnimationFragment", "No swiper data found")
                    }
                } else {
                    Log.e(
                        "AnimationFragment",
                        "Error:${response.code()} - ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<SwiperResponse>, t: Throwable) {
                Log.e("AnimationFragment", "Network Error: ${t.message}")
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        movieAdapter = MovieAdapter(mutableListOf(), binding.scrollView)
        binding.recyclerView.adapter = movieAdapter
    }

    private fun fetchTvSeries(page: Int, pageSize: Int) {
        isLoading = true

        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animationApi = retrofit.create(AnimationApi::class.java)
        val call = animationApi.getTv(page, pageSize)

        call.enqueue(object : Callback<AnimationResponse> {
            override fun onResponse(
                call: Call<AnimationResponse>,
                response: Response<AnimationResponse>
            ) {
                if (response.isSuccessful) {
                    val animationData = response.body()?.data
                    if (animationData != null) {
                        totalPages = (animationData.totalRecords + pageSize - 1) / pageSize
                        val animationList = animationData.records

                        if (animationList.isNotEmpty()) {
                            // 将新数据追加到现有数据中
                            movieAdapter.updateMovies(animationList)
                        }
                    }
                } else {
                    Log.e(
                        "MoviesFragment",
                        "Error: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                    ErrorHandler.handleUnsuccessfulResponse(
                        binding.root.context,
                        this::class.java.simpleName
                    )
                }
                isLoading = false
            }

            override fun onFailure(call: Call<AnimationResponse>, t: Throwable) {
                Log.e("MoviesFragment", "Network Error: ${t.message}")
                isLoading = false
                ErrorHandler.handleFailure(
                    t,
                    binding.root.context,
                    this::class.java.simpleName
                )
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}