package com.example.penmediatv

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.KeyEvent
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
                binding.bgCollection.background =
                    ContextCompat.getDrawable(requireContext(), R.color.button_pressed)
                binding.myCollection.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
                binding.myCollection.strokeWidth = 6
            } else {
                binding.bgCollection.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.collection_bg)
                binding.myCollection.strokeWidth = 0
            }
        }
        binding.userAgreement.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.bgAgreement.background =
                    ContextCompat.getDrawable(requireContext(), R.color.button_pressed)
                binding.userAgreement.strokeWidth = 6
                binding.userAgreement.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                binding.bgAgreement.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.agreement_bg)
                binding.userAgreement.strokeWidth = 0
            }
        }
        binding.recentlyReviewed.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.bgRecently.background =
                    ContextCompat.getDrawable(requireContext(), R.color.button_pressed)
                binding.recentlyReviewed.strokeColor = ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
                binding.recentlyReviewed.strokeWidth = 6
            } else {
                binding.bgRecently.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.recently_bg)
                binding.recentlyReviewed.strokeWidth = 0
            }
        }
        binding.aboutUs.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.bgAbout.background =
                    ContextCompat.getDrawable(requireContext(), R.color.button_pressed)
                binding.item5.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.item5.strokeWidth = 6
            } else {
                binding.bgAbout.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.about_us_bg)
                binding.item5.strokeWidth = 0
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
