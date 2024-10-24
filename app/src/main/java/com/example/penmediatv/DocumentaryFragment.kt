package com.example.penmediatv

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.SwiperResponse
import com.example.penmediatv.databinding.FragmentDocumentaryBinding
import com.example.penmediatv.databinding.FragmentTvserivesBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DocumentaryFragment : Fragment() {
    private var _binding: FragmentDocumentaryBinding? = null
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
        _binding = FragmentDocumentaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 获取屏幕高度并设置ImageView高度
        val screenHeight = resources.displayMetrics.heightPixels
        val layoutParams = binding.bgTvSeries.layoutParams
        layoutParams.height = screenHeight
        binding.bgTvSeries.layoutParams = layoutParams
        setupRecyclerView()
        fetchAnimations(currentPage, pageSize)
        fetchSwiperDocumentary()

        // 5秒后自动切换焦点到第二个卡片
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.card2.requestFocus()
//        }, 5000)

    }

    private fun fetchSwiperDocumentary() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animationApi = retrofit.create(AnimationApi::class.java)
        val call = animationApi.getSwiperDocumentary()

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
                        binding.titleText.text = swiperDate[0].videoNameEn
                        Glide.with(requireContext())
                            .load(swiperDate[0].videoCover)
                            .into(binding.moviePicZero)
                        binding.movieTitleZero.text = swiperDate[0].videoNameEn
                        binding.movieDescZero.text = swiperDate[0].videoDesc
                        binding.movieEpisodeZero.text =
                            StringBuilder().append("全").append(swiperDate[0].episode).append("集")
                                .toString()
                        Glide.with(requireContext())
                            .load(swiperDate[1].videoCover)
                            .into(binding.moviePicOne)
                        binding.movieTitleOne.text = swiperDate[1].videoNameEn
                        binding.movieDescOne.text = swiperDate[1].videoDesc
                        binding.movieEpisodeOne.text =
                            StringBuilder().append("全").append(swiperDate[1].episode).append("集")
                                .toString()
                        Glide.with(requireContext())
                            .load(swiperDate[2].videoCover)
                            .into(binding.moviePicTwo)
                        binding.movieTitleTwo.text = swiperDate[2].videoNameEn
                        binding.movieDescTwo.text = swiperDate[2].videoDesc
                        binding.movieEpisodeTwo.text =
                            StringBuilder().append("全").append(swiperDate[2].episode).append("集")
                                .toString()
                        // 设置焦点变化监听器来切换图片
                        binding.card0.setOnFocusChangeListener { _, hasFocus ->
                            if (hasFocus) {
                                Glide.with(requireContext())
                                    .load(swiperDate[0].videoCover)
                                    .into(binding.bgTvSeries)
                                binding.titleText.text = swiperDate[0].videoNameEn
                                // 增加卡片的Elevation来增加阴影效果
                                binding.card0.cardElevation = 15f
                                // 使用ScaleAnimation使卡片尺寸变大
                                val scaleUp = ScaleAnimation(
                                    1f, 1.1f, 1f, 1.1f,  // x, y方向放大到1.1倍
                                    Animation.RELATIVE_TO_SELF, 0.5f,  // X轴中心点为卡片自身的中心
                                    Animation.RELATIVE_TO_SELF, 0.5f   // Y轴中心点为卡片自身的中心
                                )
                                scaleUp.fillAfter = true // 动画结束后保持放大的状态
                                scaleUp.duration = 300 // 动画持续时间300ms
                                binding.card0.startAnimation(scaleUp)
                            } else {
                                // 恢复默认Elevation
                                binding.card0.cardElevation = 6f // 恢复到默认的阴影高度

                                // 使用ScaleAnimation使卡片恢复原始尺寸
                                val scaleDown = ScaleAnimation(
                                    1.1f, 1f, 1.1f, 1f,  // 从1.1倍恢复到原始大小
                                    Animation.RELATIVE_TO_SELF, 0.5f,
                                    Animation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleDown.fillAfter = true
                                scaleDown.duration = 300
                                binding.card0.startAnimation(scaleDown)
                            }
                        }
                        binding.card0.setOnKeyListener { view, keyCode, keyEvent ->
                            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                // 查找焦点的目标View
                                val nextFocusView = view.focusSearch(View.FOCUS_DOWN)
                                nextFocusView?.requestFocus()
                                return@setOnKeyListener true
                            }
                            false
                        }
                        binding.card0.setOnClickListener {
                            if (swiperDate[0].episode <= 1) {
                                val intent = Intent(context, MovieDetailsActivity::class.java)
                                intent.putExtra(
                                    "VIDEO_ID",
                                    swiperDate[0].videoId
                                ) // 将 videoId 传递给 MovieDetailsActivity
                                startActivity(intent)
                            } else {
                                val intent = Intent(context, TvDetailsActivity::class.java)
                                intent.putExtra("VIDEO_ID", swiperDate[0].videoId)
                                intent.putExtra("VIDEO_EPISODE", swiperDate[0].episode)
                                startActivity(intent)
                            }
                        }
                        binding.card1.setOnClickListener {
                            if (swiperDate[1].episode <= 1) {
                                val intent = Intent(context, MovieDetailsActivity::class.java)
                                intent.putExtra(
                                    "VIDEO_ID",
                                    swiperDate[1].videoId
                                ) // 将 videoId 传递给 MovieDetailsActivity
                                startActivity(intent)
                            } else {
                                val intent = Intent(context, TvDetailsActivity::class.java)
                                intent.putExtra("VIDEO_ID", swiperDate[1].videoId)
                                intent.putExtra("VIDEO_EPISODE", swiperDate[1].episode)
                                startActivity(intent)
                            }
                        }
                        binding.card2.setOnClickListener {
                            if (swiperDate[2].episode <= 1) {
                                val intent = Intent(context, MovieDetailsActivity::class.java)
                                intent.putExtra(
                                    "VIDEO_ID",
                                    swiperDate[2].videoId
                                ) // 将 videoId 传递给 MovieDetailsActivity
                                startActivity(intent)
                            } else {
                                val intent = Intent(context, TvDetailsActivity::class.java)
                                intent.putExtra("VIDEO_ID", swiperDate[2].videoId)
                                intent.putExtra("VIDEO_EPISODE", swiperDate[2].episode)
                                startActivity(intent)
                            }
                        }
                        binding.card1.setOnFocusChangeListener { _, hasFocus ->
                            if (hasFocus) {
                                Glide.with(requireContext())
                                    .load(swiperDate[1].videoCover)
                                    .into(binding.bgTvSeries)
                                binding.titleText.text = swiperDate[1].videoNameEn
                                // 增加卡片的Elevation来增加阴影效果
                                binding.card1.cardElevation = 15f
                                val scaleUp = ScaleAnimation(
                                    1f, 1.1f, 1f, 1.1f,  // x, y方向放大到1.1倍
                                    Animation.RELATIVE_TO_SELF, 0.5f,  // X轴中心点为卡片自身的中心
                                    Animation.RELATIVE_TO_SELF, 0.5f   // Y轴中心点为卡片自身的中心
                                )
                                scaleUp.fillAfter = true // 动画结束后保持放大的状态
                                scaleUp.duration = 300 // 动画持续时间300ms
                                binding.card1.startAnimation(scaleUp)
                            } else {
                                // 恢复默认Elevation
                                binding.card1.cardElevation = 6f // 恢复到默认的阴影高度
                                // 使用ScaleAnimation使卡片恢复原始尺寸
                                val scaleDown = ScaleAnimation(
                                    1.1f, 1f, 1.1f, 1f,  // 从1.1倍恢复到原始大小
                                    Animation.RELATIVE_TO_SELF, 0.5f,
                                    Animation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleDown.fillAfter = true
                                scaleDown.duration = 300
                                binding.card1.startAnimation(scaleDown)
                            }
                        }
                        binding.card1.setOnKeyListener { view, keyCode, keyEvent ->
                            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                // 查找焦点的目标View
                                val nextFocusView = view.focusSearch(View.FOCUS_DOWN)
                                nextFocusView?.requestFocus()
                                return@setOnKeyListener true
                            }
                            false
                        }

                        binding.card2.setOnFocusChangeListener { _, hasFocus ->
                            if (hasFocus) {
                                Glide.with(requireContext())
                                    .load(swiperDate[2].videoCover)
                                    .into(binding.bgTvSeries)
                                binding.titleText.text = swiperDate[2].videoNameEn
                                // 增加卡片的Elevation来增加阴影效果
                                binding.card2.cardElevation = 15f

                                val scaleUp = ScaleAnimation(
                                    1f, 1.1f, 1f, 1.1f,  // x, y方向放大到1.1倍
                                    Animation.RELATIVE_TO_SELF, 0.5f,  // X轴中心点为卡片自身的中心
                                    Animation.RELATIVE_TO_SELF, 0.5f   // Y轴中心点为卡片自身的中心
                                )
                                scaleUp.fillAfter = true // 动画结束后保持放大的状态
                                scaleUp.duration = 300 // 动画持续时间300ms
                                binding.card2.startAnimation(scaleUp)
                            } else {
                                // 恢复默认Elevation
                                binding.card2.cardElevation = 6f // 恢复到默认的阴影高度

                                // 使用ScaleAnimation使卡片恢复原始尺寸
                                val scaleDown = ScaleAnimation(
                                    1.1f, 1f, 1.1f, 1f,  // 从1.1倍恢复到原始大小
                                    Animation.RELATIVE_TO_SELF, 0.5f,
                                    Animation.RELATIVE_TO_SELF, 0.5f
                                )
                                scaleDown.fillAfter = true
                                scaleDown.duration = 300
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
        val call = animationApi.getDocumentary(page, pageSize)

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