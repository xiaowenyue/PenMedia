package com.example.penmediatv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.company.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button_pressed))
            } else {
                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button))
            }
        }
        binding.software.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button_pressed))
            } else {
                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button))
            }
        }
        binding.version.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button_pressed))
            } else {
                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button))
            }
        }
        binding.company.setOnClickListener {
            binding.company.setBackgroundColor(resources.getColor(R.color.button_pressed))
        }
        binding.software.setOnClickListener {
            binding.software.setBackgroundColor(resources.getColor(R.color.button_pressed))
        }
        binding.version.setOnClickListener {
            binding.version.setBackgroundColor(resources.getColor(R.color.button_pressed))
        }
    }
}