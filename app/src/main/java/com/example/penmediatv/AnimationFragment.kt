package com.example.penmediatv

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
import androidx.viewpager2.widget.ViewPager2
import com.example.penmediatv.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment() {
    private var _binding: FragmentAnimationBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoTitleCarouselAdapter
    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val slideInterval: Long = 3000 // 滚动间隔时间，单位为毫秒

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        binding.recyclerView.adapter = MovieAdapter(getMovies())
        val items = listOf(
            Movie("Title 0", R.drawable.movie, "Details 1", "Time 1"),
            Movie("Title 1", R.drawable.ic_search, "Details 2", "Time 2"),
            Movie("Title 2", R.drawable.ic_history, "Details 3", "Time 3")
        )
        adapter = NoTitleCarouselAdapter(items)
        binding.viewPager.adapter = adapter
        setupIndicators(items.size)
        setCurrentIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        binding.viewPager.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Log.v("MoviesFragment", "viewPager has focus")
                Toast.makeText(context, "viewPager has focus", Toast.LENGTH_SHORT).show()
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
        startAutoSlide()
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

    private fun getMovies(): List<Movie> {
        // Generate dummy movie data
        return listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.ic_search),
            Movie("Movie 3", R.drawable.ic_history),
            Movie("Movie 4", R.drawable.ic_mine),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
            Movie("Movie 7", R.drawable.ic_mine),
            Movie("Movie 8", R.drawable.ic_search),
            Movie("Movie 9", R.drawable.ic_history),
            Movie("Movie 10", R.drawable.ic_mine),
            Movie("Movie 11", R.drawable.ic_search),
            Movie("Movie 12", R.drawable.ic_history),
            Movie("Movie 13", R.drawable.ic_mine),
            Movie("Movie 14", R.drawable.ic_search),
            Movie("Movie 15", R.drawable.ic_history),
            Movie("Movie 16", R.drawable.ic_mine)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoSlide()
        _binding = null
    }
}