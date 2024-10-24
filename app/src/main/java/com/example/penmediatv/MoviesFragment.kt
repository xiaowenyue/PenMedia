package com.example.penmediatv

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.SwiperResponse
import com.example.penmediatv.databinding.FragmentMoviesBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarouselAdapter
    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val slideInterval: Long = 3000 // 滚动间隔时间，单位为毫秒
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
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchAnimations(currentPage, pageSize)
        fetchSwiperMovies()

        binding.viewPagerLayout.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Log.v("MoviesFragment", "viewPager has focus")
                view.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN) {
                        when (keyCode) {
                            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                Log.v("MoviesFragment", "viewPager right key pressed")
                                // 右键按下时切换到下一个页面
                                stopAutoSlide()
                                val currentItem = binding.viewPager.currentItem
                                val nextItem =
                                    if (currentItem == adapter.itemCount - 1) 0 else currentItem + 1
                                binding.viewPager.setCurrentItem(nextItem, true)
                                startAutoSlide() // 切换后重新开始自动播放
                                true
                            }

                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                Log.v("MoviesFragment", "viewPager left key pressed")
                                // 左键按下时切换到上一个页面
                                stopAutoSlide()
                                val currentItem = binding.viewPager.currentItem
                                val previousItem =
                                    if (currentItem == 0) adapter.itemCount - 1 else currentItem - 1
                                binding.viewPager.setCurrentItem(previousItem, true)
                                startAutoSlide() // 切换后重新开始自动播放
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                }
            } else {
                // 如果失去焦点，可以移除按键监听
                view.setOnKeyListener(null)
            }
        }
        binding.viewPagerLayout.setOnClickListener {
            val currentPosition = binding.viewPager.currentItem // 获取当前显示的页面索引
            val currentItem = adapter.items[currentPosition] // 从适配器获取当前显示的 SwiperItem
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("VIDEO_ID", currentItem.videoId) // 将 videoId 传递给 MovieDetailsActivity
            startActivity(intent)
        }
    }
    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 5)
        binding.recyclerView.layoutManager = gridLayoutManager
        movieAdapter = MovieAdapter(mutableListOf(), binding.scrollView)
        binding.recyclerView.adapter = movieAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 获取最后一个可见项的索引
                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

                // 判断是否接近底部，并且确保没有正在加载数据
                if (!isLoading && firstVisibleItemPosition + visibleItemCount >= totalItemCount - 2 && currentPage < totalPages) {
                    currentPage++
                    fetchAnimations(currentPage, pageSize)
                }
            }
        })
    }
    private fun fetchAnimations(page: Int, pageSize: Int) {
        isLoading = true

        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animationApi = retrofit.create(AnimationApi::class.java)
        val call = animationApi.getMovies(page, pageSize)

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
    private fun startAutoSlide() {
        runnable = object : Runnable {
            override fun run() {
                if (isAdded && _binding != null) {
                    val currentItem = binding.viewPager.currentItem
                    val itemCount = adapter.itemCount
                    val nextItem =
                        if (currentItem == itemCount - 1) 0 else currentItem + 1
                    binding.viewPager.setCurrentItem(nextItem, true)
                    handler.postDelayed(this, slideInterval)
                }
            }
        }
        handler.postDelayed(runnable!!, slideInterval)
    }
    private fun stopAutoSlide() {
        handler.removeCallbacks(runnable!!)
    }
    private fun fetchSwiperMovies() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animationApi = retrofit.create(AnimationApi::class.java)
        val call = animationApi.getSwiperMovie()

        call.enqueue(object : Callback<SwiperResponse> {
            override fun onResponse(
                call: Call<SwiperResponse>,
                response: Response<SwiperResponse>
            ) {
                if (response.isSuccessful) {
                    val swiperDate = response.body()?.data
                    if (swiperDate != null && swiperDate.isNotEmpty()) {
                        // 数据适配到轮播图适配器
                        adapter = CarouselAdapter(swiperDate)
                        binding.viewPager.adapter = adapter
                        // 设置轮播图指示器
                        setupIndicators(swiperDate.size)
                        setCurrentIndicator(0)

                        // 设置页面更改回调
                        binding.viewPager.registerOnPageChangeCallback(object :
                            ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                setCurrentIndicator(position)
                            }
                        })

                        // 数据加载完成并设置完 adapter 后，启动自动轮播
                        startAutoSlide()
                    } else {
                        Log.e("MoviesFragment", "No swiper data found")
                    }
                } else {
                    Log.e(
                        "MoviesFragment",
                        "Error:${response.code()} - ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<SwiperResponse>, t: Throwable) {
                Log.e("MoviesFragment", "Network Error: ${t.message}")
            }
        })
    }
    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(80, 20)
        layoutParams.setMargins(10, 0, 10, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i]?.setImageDrawable(
                context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.indicator_inactive
                    )
                }
            )
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.indicator_active
                        )
                    }
                )
            } else {
                imageView.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.indicator_inactive
                        )
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoSlide()
        _binding = null
    }
}