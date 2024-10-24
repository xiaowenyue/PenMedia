package com.example.penmediatv

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.Data.AnimationItem
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.HomeResponse
import com.example.penmediatv.Data.SwiperResourceItem
import com.example.penmediatv.databinding.FragmentHomeBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        setupRecyclerView()
        fetchVideos()
        binding.cv0.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv0.strokeWidth = 6
                binding.cv0.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.llContent0.visibility = View.VISIBLE
            } else {
                binding.cv0.strokeWidth = 0
                binding.llContent0.visibility = View.GONE
            }
        }
        binding.cv1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv1.strokeWidth = 6
                binding.cv1.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.llContent1.visibility = View.VISIBLE
            } else {
                binding.cv1.strokeWidth = 0
                binding.llContent1.visibility = View.GONE
            }
        }
        binding.cv2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv2.strokeWidth = 6
                binding.cv2.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.05f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.ll2.startAnimation(scaleUp)
                val location = IntArray(2)
                binding.cv2.getLocationOnScreen(location)
                binding.scrollView.smoothScrollTo(
                    0,
                    location[1] - binding.scrollView.height / 2 + binding.cv2.height / 2
                )
            } else {
                binding.cv2.strokeWidth = 0
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.05f, 1f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.ll2.startAnimation(scaleDown)
            }
        }
        binding.cv3.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv3.strokeWidth = 6
                binding.cv3.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.05f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.ll3.startAnimation(scaleUp)
            } else {
                binding.cv3.strokeWidth = 0
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.05f, 1f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.ll3.startAnimation(scaleDown)
            }
        }
        binding.cv3.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                val nextFocusView = view.focusSearch(View.FOCUS_LEFT)
                nextFocusView?.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
        binding.cv4.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv4.strokeWidth = 6
                binding.cv4.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.05f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.ll4.startAnimation(scaleUp)
            } else {
                binding.cv4.strokeWidth = 0
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.05f, 1f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.ll4.startAnimation(scaleDown)
            }
        }
        binding.cv4.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                val nextFocusView = view.focusSearch(View.FOCUS_LEFT)
                nextFocusView?.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
        binding.cv5.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv5.strokeWidth = 6
                binding.cv5.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.05f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.ll5.startAnimation(scaleUp)
            } else {
                binding.cv5.strokeWidth = 0
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.05f, 1f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.ll5.startAnimation(scaleDown)
            }
        }
        binding.cv5.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                val nextFocusView = view.focusSearch(View.FOCUS_LEFT)
                nextFocusView?.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        movieAdapter = MovieAdapter(mutableListOf(), binding.scrollView)
        binding.recyclerView.adapter = movieAdapter
    }

    private fun fetchVideos() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val animationApi = retrofit.create(AnimationApi::class.java)
        val call = animationApi.getHomeResource()

        call.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(
                call: Call<HomeResponse>,
                response: Response<HomeResponse>
            ) {
                if (response.isSuccessful) {
                    val animationData = response.body()?.data
                    if (animationData != null) {
                        val animationList = animationData.recommandResourceList
                        if (animationList.isNotEmpty()) {
                            // 将新数据追加到现有数据中
                            movieAdapter.updateMovies(animationList.map {
                                convertSwiperResourceToAnimationItem(
                                    it
                                )
                            })
                        }
                    }
                } else {
                    Log.e(
                        "HomeFragment",
                        "Error: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                    ErrorHandler.handleUnsuccessfulResponse(
                        binding.root.context,
                        this::class.java.simpleName
                    )
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("HomeFragment", "Network Error: ${t.message}")
                ErrorHandler.handleFailure(
                    t,
                    binding.root.context,
                    this::class.java.simpleName
                )
            }
        })
    }

    fun convertSwiperResourceToAnimationItem(swipeItem: SwiperResourceItem): AnimationItem {
        return AnimationItem(
            videoNameEn = swipeItem.videoNameEn,
            videoNameZh = swipeItem.videoNameZh,
            videoCover = swipeItem.videoCover,
            episode = swipeItem.episode,
            videoId = swipeItem.videoId,
            subTitle = swipeItem.subTitle,
            videoDesc = "", // 假设otherInfo中有description字段
            otherInfo = swipeItem.otherInfo
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

