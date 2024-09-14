package com.example.penmediatv

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.FragmentDocumentaryBinding
import com.example.penmediatv.databinding.FragmentTvserivesBinding

class DocumentaryFragment : Fragment() {
    private var _binding: FragmentDocumentaryBinding? = null
    private val binding get() = _binding!!

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

        binding.listTvSeries.layoutManager = GridLayoutManager(context, 5)
        binding.listTvSeries.adapter = MovieAdapter(getMovies())
        binding.bgTvSeries.setImageResource(R.drawable.movie) // 设置第一个卡片对应的图片
        // 设置焦点变化监听器来切换图片
        binding.card0.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val color = ContextCompat.getColor(requireContext(), R.color.orange)
                binding.card0.setBackgroundColor(color)
                binding.bgTvSeries.setImageResource(R.drawable.movie)
            } else {
                binding.card0.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
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
                binding.card1.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.orange
                    )
                )
                binding.bgTvSeries.setImageResource(R.drawable.ic_mine)
            } else {
                binding.card1.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.card1
                    )
                )
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
                binding.card2.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.orange
                    )
                )
                binding.bgTvSeries.setImageResource(R.drawable.ic_history)
            } else {
                binding.card2.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.card2
                    )
                )
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