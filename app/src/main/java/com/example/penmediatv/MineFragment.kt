package com.example.penmediatv

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.penmediatv.databinding.FragmentMineBinding

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        binding.aboutUs.setOnClickListener {
            val intent = Intent(context, AboutUsActivity::class.java)
            startActivity(intent)
        }
        binding.myCollection.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.myCollection.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
                binding.myCollection.strokeWidth = 6
                binding.myCollection.startAnimation(scaleUp)
            } else {
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.myCollection.strokeWidth = 0
                binding.myCollection.startAnimation(scaleDown)
            }
        }
        binding.userAgreement.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.userAgreement.strokeWidth = 6
                binding.userAgreement.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
                binding.userAgreement.startAnimation(scaleUp)
            } else {
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.userAgreement.strokeWidth = 0
                binding.userAgreement.startAnimation(scaleDown)
            }
        }
        binding.recentlyReviewed.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.recentlyReviewed.strokeColor = ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
                binding.recentlyReviewed.strokeWidth = 6
                binding.recentlyReviewed.startAnimation(scaleUp)
            } else {
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.recentlyReviewed.strokeWidth = 0
                binding.recentlyReviewed.startAnimation(scaleDown)
            }
        }
        binding.aboutUs.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val scaleUp = ScaleAnimation(
                    1f, 1.1f, 1f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleUp.duration = 300
                scaleUp.fillAfter = true
                binding.item5.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.item5.strokeWidth = 6
                binding.item5.startAnimation(scaleUp)
            } else {
                binding.item5.strokeWidth = 0
                val scaleDown = ScaleAnimation(
                    1.1f, 1f, 1.1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleDown.duration = 300
                scaleDown.fillAfter = true
                binding.item5.startAnimation(scaleDown)
            }
        }
        binding.aboutUs.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                // 查找焦点的目标View
                val nextFocusView = view.focusSearch(View.FOCUS_LEFT)
                nextFocusView?.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
        binding.myCollection.setOnClickListener {
            val intent = Intent(context, MyCollectionActivity::class.java)
            startActivity(intent)
        }
        binding.userAgreement.setOnClickListener {
            val intent = Intent(context, UserAgreementActivity::class.java)
            startActivity(intent)
        }
        binding.recentlyReviewed.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity?.binding?.navHistory?.requestFocus()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
