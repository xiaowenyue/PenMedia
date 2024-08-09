package com.example.penmediatv

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.penmediatv.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.firstFocusableElement.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //显示标题和副标题
                binding.title.visibility = View.VISIBLE
                binding.subtitle.visibility = View.VISIBLE

                //放大效果
                v.animate().setDuration(200).scaleX(1.1f).scaleY(1.1f).start()
                v.stateListAnimator =
                    AnimatorInflater.loadStateListAnimator(context, R.animator.scale_up)
            } else {
                //隐藏标题和副标题
                binding.title.visibility = View.GONE
                binding.subtitle.visibility = View.GONE

                //恢复原始大小
                v.animate().setDuration(200).scaleX(1.0f).scaleY(1.0f).start()
                v.stateListAnimator =
                    AnimatorInflater.loadStateListAnimator(context, R.animator.scale_down)
            }
        }
        binding.firstFocusableElement.setOnClickListener {}

        binding.firstFocusableElement0.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //显示标题和副标题
                binding.title0.visibility = View.VISIBLE
                binding.subtitle0.visibility = View.VISIBLE

                //放大效果
                v.animate().setDuration(200).scaleX(1.1f).scaleY(1.1f).start()
                v.stateListAnimator =
                    AnimatorInflater.loadStateListAnimator(context, R.animator.scale_up)
            } else {
                //隐藏标题和副标题
                binding.title0.visibility = View.GONE
                binding.subtitle0.visibility = View.GONE

                //恢复原始大小
                v.animate().setDuration(200).scaleX(1.0f).scaleY(1.0f).start()
                v.stateListAnimator =
                    AnimatorInflater.loadStateListAnimator(context, R.animator.scale_down)
            }
        }
        binding.firstFocusableElement0.setOnClickListener {}

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}