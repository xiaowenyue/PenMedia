package com.example.penmediatv

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.penmediatv.databinding.FragmentMineBinding

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        binding.movieCard.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // 当组件获得焦点时，增加阴影并设置边框
                binding.movieCard.cardElevation = 4f // 增加阴影
                binding.headerImage.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.border
                ) // 设置边框
                binding.movieCard.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.border
                ) // 设置边框
            } else {
                // 当组件失去焦点时，恢复原始状态
                binding.movieCard.cardElevation = 0f // 移除阴影
                binding.headerImage.background = null // 移除边框
            }
        }
        binding.movieCard.setOnClickListener { view ->
            // 当组件获得焦点时，增加阴影并设置边框
            binding.movieCard.cardElevation = 4f // 增加阴影
            binding.movieCard.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.border
            ) // 设置边框 移除边框
            Toast.makeText(context, "click card", Toast.LENGTH_SHORT).show()
        }
        binding.movieCard0.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.movieCard0.cardElevation = 12f // 增加阴影效果
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.setStroke(4f.toInt(), ContextCompat.getColor(requireContext(), R.color.black))
                binding.movieCard0.background = shape
            } else {
                binding.movieCard0.cardElevation = 0f // 恢复默认阴影效果
                binding.movieCard0.background = null // 移除边框
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
