package com.example.penmediatv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.FragmentTvserivesBinding

class TvSeriesFragment : Fragment() {
    private var _binding: FragmentTvserivesBinding? = null
    private val binding get() = _binding!!

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
        binding.listTvSeries.layoutManager = GridLayoutManager(context, 5)
        binding.listTvSeries.adapter = MovieAdapter(getMovies(), binding.scrollView)
        // 设置默认聚焦第一个卡片
//        binding.card1.requestFocus()
        binding.bgTvSeries.setImageResource(R.drawable.movie) // 设置第一个卡片对应的图片

        // 设置焦点变化监听器来切换图片
        binding.card1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.card1.strokeWidth = 6
                binding.card1.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.bgTvSeries.setImageResource(R.drawable.movie)
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
                binding.card2.strokeWidth = 6
                binding.card2.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.bgTvSeries.setImageResource(R.drawable.ic_mine)
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

        // 5秒后自动切换焦点到第二个卡片
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.card2.requestFocus()
//        }, 5000)

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
        _binding = null
    }
}