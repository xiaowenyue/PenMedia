package com.example.penmediatv

import android.content.Intent
import android.graphics.Color
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        binding.aboutUs.setOnFocusChangeListener { view, hasFocus ->

        }

        binding.aboutUs.setOnClickListener {
            Toast.makeText(context, "About Us", Toast.LENGTH_SHORT).show()
        }

        binding.myCollection.setOnClickListener {
            Toast.makeText(context, "My collection", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MyCollectionActivity::class.java)
            startActivity(intent)
        }

        binding.userAgreement.setOnClickListener {}
        binding.recentlyReviewed.setOnClickListener {}
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
