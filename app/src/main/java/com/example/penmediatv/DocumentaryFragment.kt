package com.example.penmediatv

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
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.databinding.FragmentDocumentaryBinding
import com.example.penmediatv.databinding.FragmentTvserivesBinding
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
        binding.bgTvSeries.setImageResource(R.drawable.movie) // 设置第一个卡片对应的图片
        // 设置焦点变化监听器来切换图片
        binding.card0.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 增加卡片的Elevation来增加阴影效果
                binding.card0.cardElevation = 15f
                binding.bgTvSeries.setImageResource(R.drawable.movie)
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
//                binding.card0.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
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

        binding.card1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.bgTvSeries.setImageResource(R.drawable.ic_mine)
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
                binding.bgTvSeries.setImageResource(R.drawable.ic_history)
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

        // 5秒后自动切换焦点到第二个卡片
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.card2.requestFocus()
//        }, 5000)

    }
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        movieAdapter = MovieAdapter(mutableListOf(), binding.scrollView)
        binding.recyclerView.adapter = movieAdapter
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
                }
                isLoading = false
            }

            override fun onFailure(call: Call<AnimationResponse>, t: Throwable) {
                Log.e("MoviesFragment", "Network Error: ${t.message}")
                isLoading = false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}